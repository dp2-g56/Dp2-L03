
package services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;

import repositories.PositionRepository;
import domain.Actor;
import domain.Application;
import domain.Company;
import domain.Position;
import domain.Problem;
import domain.Status;
import forms.FormObjectPositionProblemCheckbox;

@Service
@Transactional
public class PositionService {

	@Autowired
	private PositionRepository positionRepository;

	@Autowired
	private CompanyService companyService;

	@Autowired
	private ProblemService problemService;

	@Autowired
	private ApplicationService applicationService;

	@Autowired
	private HackerService hackerService;

	public List<Position> findAll() {
		return this.positionRepository.findAll();
	}

	// --------------------------------BASIC CRUDS----------------------------
	// -----------------------------------------------------------------------
	public Position save(Position position) {
		return this.positionRepository.save(position);
	}

	public Position findOne(int positionId) {
		return this.positionRepository.findOne(positionId);
	}

	// -------------------------------LIST-----------------------------------
	// ----------------------------------------------------------------------

	// ---------------------------------------CREATE-------------------------
	// ----------------------------------------------------------------------
	public Position createPosition() {
		this.companyService.loggedAsCompany();

		Position position = new Position();

		String ticker = this.generateTicker();

		List<Application> applications = new ArrayList<>();
		List<Problem> problems = new ArrayList<>();
		List<String> requiredSkills = new ArrayList<>();
		List<String> requiredTecnologies = new ArrayList<>();

		position.setApplications(applications);
		position.setDeadline(null);
		position.setDescription("");
		position.setIsCancelled(false);
		position.setIsDraftMode(true);
		position.setOfferedSalary(0.0);
		position.setProblems(problems);
		position.setRequiredProfile("");
		position.setRequiredSkills(requiredSkills);
		position.setRequiredTecnologies(requiredTecnologies);
		position.setTicker(ticker);
		position.setTitle("");

		return position;
	}

	// TICKER GENERATOR----------------------------------------------------
	private String generateTicker() {
		String res = "";
		Company loggedCompany = this.companyService.loggedCompany();

		String companyName = loggedCompany.getCompanyName();

		if (companyName.length() >= 4) {
			res = companyName.substring(0, 4) + "-";
		} else {
			int numberOfX = 4 - companyName.length();
			res = companyName;
			for (int i = 1; i <= numberOfX; i++) {
				res = res + "X";
			}
			res = res + "-";
		}

		String numbers = "0123456789";
		StringBuilder number = new StringBuilder();
		Random rnd = new Random();

		while (number.length() < 4) { // Es posible que haya que cambiarlo a 5
			int index = (int) (rnd.nextFloat() * numbers.length());
			number.append(numbers.charAt(index));
		}
		String numberStr = number.toString();

		res = res + numberStr;

		List<Position> lc = this.positionRepository.findAll();

		for (Position c : lc) {
			if (c.getTicker() == res) {
				return this.generateTicker();
			}
		}
		return res;
	}

	// ---------------------------------------EDIT---------------------------
	// ----------------------------------------------------------------------

	public Position edit(Position position, Date deadline, String description, Boolean isDraftMode,
			Double offeredSalary, List<Problem> problems, String requiredProfile, List<String> requiredSkills,
			List<String> requiredTecnologies) {
		// Security
		Company loggedCompany = this.companyService.loggedCompany();
		Assert.isTrue(loggedCompany.getPositions().contains(position));
		Assert.isTrue(!position.getIsCancelled());
		Assert.isTrue(position.getIsDraftMode());

		List<Position> positions = loggedCompany.getPositions();
		positions.remove(position);

		position.setDeadline(deadline);
		position.setDescription(description);
		position.setIsDraftMode(isDraftMode);
		position.setOfferedSalary(offeredSalary);
		position.setProblems(problems);
		position.setRequiredProfile(requiredProfile);
		position.setRequiredSkills(requiredSkills);
		position.setRequiredTecnologies(requiredTecnologies);

		Position saved = this.save(position);
		positions.add(saved);
		loggedCompany.setPositions(positions);

		this.companyService.save(loggedCompany);

		return saved;
	}

	// ---------------------------------------DELETE-------------------------
	// ----------------------------------------------------------------------
	public void deletePosition(Position position) {
		// Security
		Company loggedCompany = this.companyService.loggedCompany();
		List<Position> positions = loggedCompany.getPositions();

		Assert.isTrue(position.getIsDraftMode()); // Nunca podrá ser cancel
		Assert.isTrue(positions.contains(position));

		// No puede tener applications si esta en draft

		// Eliminamos los problemas
		List<Problem> problems = new ArrayList<>();
		position.setProblems(problems);

		positions.remove(position);
		loggedCompany.setPositions(positions);

		this.companyService.save(loggedCompany);

		this.positionRepository.delete(position);
	}

	// ----------------------------CANCEL POSITION----------------------------------
	// -----------------------------------------------------------------------------
	public void cancelPosition(Position position) {
		// Security
		Company loggedCompany = this.companyService.loggedCompany();
		List<Position> positions = loggedCompany.getPositions();

		Assert.isTrue(positions.contains(position));
		Assert.isTrue(!position.getIsDraftMode());
		Assert.isTrue(!position.getIsCancelled());

		positions.remove(position);

		List<Application> submittedApplication = this.applicationService
				.getSubmittedApplicationCompany(position.getId());

		for (Application a : submittedApplication) {
			a.setStatus(Status.REJECTED);
			this.applicationService.save(a);
		}

		position.setIsCancelled(true);
		Position saved = this.save(position);
		positions.add(saved);
		loggedCompany.setPositions(positions);

		this.companyService.save(loggedCompany);
	}

	// -----------------------------RECONSTRUCT FORM OBJECT-------------------------
	// -----------------------------------------------------------------------------
	public FormObjectPositionProblemCheckbox prepareFormObjectPositionProblemCheckbox(int positionId) {

		Position position = this.positionRepository.findOne(positionId);

		Assert.notNull(position);
		Company loggedCompany = this.companyService.loggedCompany();
		Assert.isTrue(loggedCompany.getPositions().contains(position));
		Assert.isTrue(position.getIsDraftMode());
		Assert.isTrue(!position.getIsCancelled());

		FormObjectPositionProblemCheckbox result = new FormObjectPositionProblemCheckbox();

		List<Integer> problems = new ArrayList<>();
		for (Problem f : position.getProblems()) {
			problems.add(f.getId());
		}

		result.setDeadline(position.getDeadline());
		result.setDescription(position.getDescription());
		result.setId(position.getId());
		result.setIsDraftMode(position.getIsDraftMode());
		result.setOfferedSalary(position.getOfferedSalary());
		result.setProblems(problems);
		result.setRequiredProfile(position.getRequiredProfile());

		String requiredSkills = "";
		for (String skill : position.getRequiredSkills()) {
			requiredSkills = requiredSkills + skill + ",";
		}
		result.setRequiredSkills(requiredSkills);

		String requiredTecnologies = "";
		for (String tech : position.getRequiredTecnologies()) {
			requiredTecnologies = requiredTecnologies + tech + ",";
		}
		result.setRequiredTecnologies(requiredTecnologies);
		result.setTitle(position.getTitle());

		return result;
	}

	public Map<Integer, String> getMapAvailableProblems() {

		Company loggedCompany = this.companyService.loggedCompany();

		List<Problem> problems = this.problemService.getFinalProblemsByCompany(loggedCompany.getId());

		Map<Integer, String> map = new HashMap<>();

		for (Problem problem : problems) {
			map.put(problem.getId(), problem.getTitle());
		}

		return map;
	}

	public Position reconstructCheckBox(FormObjectPositionProblemCheckbox formObjectPositionProblemCheckbox,
			BindingResult binding) {
		Position result = new Position();

		if (formObjectPositionProblemCheckbox.getId() == 0) {
			result.setTicker(this.generateTicker());
		} else {
			result = this.positionRepository.findOne(formObjectPositionProblemCheckbox.getId());
		}
		Position result2 = this.createPosition();
		result2.setId(result.getId());
		result2.setVersion(result.getVersion());
		result2.setTicker(this.generateTicker());
		result2.setDeadline(formObjectPositionProblemCheckbox.getDeadline());
		result2.setDescription(formObjectPositionProblemCheckbox.getDescription());

		result2.setIsCancelled(false);
		result2.setIsDraftMode(formObjectPositionProblemCheckbox.getIsDraftMode());
		result2.setOfferedSalary(formObjectPositionProblemCheckbox.getOfferedSalary());
		result2.setProblems(result.getProblems());
		result2.setRequiredProfile(formObjectPositionProblemCheckbox.getRequiredProfile());

		String[] requiredSkills = formObjectPositionProblemCheckbox.getRequiredSkills().trim().split(",");
		List<String> listRequiredSkills = Arrays.asList(requiredSkills);

		result2.setRequiredSkills(listRequiredSkills);

		String[] requiredTechnologies = formObjectPositionProblemCheckbox.getRequiredTecnologies().trim().split(",");
		List<String> listRequiredTechnologies = Arrays.asList(requiredTechnologies);

		result2.setRequiredTecnologies(listRequiredTechnologies);
		result2.setTitle(formObjectPositionProblemCheckbox.getTitle());

		return result2;
	}

	public Position saveAssignList(Position position, List<Problem> problems) {

		Company loggedCompany = this.companyService.loggedCompany();

		for (Problem f : problems) {
			Assert.isTrue(loggedCompany.getProblems().contains(f) && f.getId() > 0);
		}

		if (position.getId() > 0) {
			Position positionFounded = this.findOne(position.getId());
			Assert.isTrue(positionFounded.getIsDraftMode() && loggedCompany.getPositions().contains(positionFounded));
		}

		if (!position.getIsDraftMode()) {
			Assert.isTrue(problems.size() >= 2);
		}

		position.setProblems(problems);
		Position saved = new Position();
		saved = this.positionRepository.save(position);

		loggedCompany.getPositions().remove(position);
		loggedCompany.getPositions().add(saved);

		this.companyService.save(loggedCompany);

		return saved;
	}

	// --------------------------------------------DELETE-----------------------------------------------------
	// -------------------------------------------------------------------------------------------------------
	public void deletePositionWithId(int positionId) {

		Position position = this.positionRepository.findOne(positionId);

		Company loggedCompany = this.companyService.loggedCompany();

		Assert.notNull(position);
		Assert.isTrue(loggedCompany.getPositions().contains(position));
		Assert.isTrue(position.getIsDraftMode());
		Assert.isTrue(!position.getIsCancelled());

		position.setProblems(new ArrayList<Problem>());

		loggedCompany.getPositions().remove(position);
		this.companyService.save(loggedCompany);

		this.positionRepository.delete(position);

	}

	public List<Position> getFinalPositions() {
		return this.positionRepository.getFinalPositions();
	}

	public List<Position> getFinalPositionsAndNotCancelled() {
		List<Position> res = new ArrayList<Position>();
		for (Position p : this.positionRepository.getFinalPositions()) {
			if (p.getIsCancelled().equals(false)) {
				res.add(p);
			}
		}
		return res;
	}

	// --------------------------------------AUXILIAR
	// METHODS-------------------------//

	public void flush() {
		this.positionRepository.flush();
	}

	public List<Problem> getProblemsOfPosition(int idPosition) {
		return this.positionRepository.getProblemsOfPosition(idPosition);
	}

	public List<Position> positionsFiltered(String word) {
		return this.positionRepository.positionsFiltered("%" + word + "%");
	}

	public List<String> getSkillsAsHacker(int positionId) {
		this.hackerService.loggedAsHacker();
		Position position = this.findOne(positionId);
		Assert.isTrue(this.getFinalPositions().contains(position));
		return position.getRequiredSkills();
	}

	public List<String> getTechnologiesAsHacker(int positionId) {
		this.hackerService.loggedAsHacker();
		Position position = this.findOne(positionId);
		Assert.isTrue(this.getFinalPositions().contains(position));
		return position.getRequiredTecnologies();
	}

	/*
	 * public List<Position> positionsOfApplicationOfHacker(Hacker hacker) { return
	 * this.positionRepository.positionsOfApplicationOfHacker(hacker); }
	 */

	public void deleteInBatch(List<Position> positions) {
		this.positionRepository.deleteInBatch(positions);

	}

	public void delete(Position position) {
		this.positionRepository.delete(position);

	}

	public Actor getActorWithPosition(int positionId) {
		return this.positionRepository.getActorWithPosition(positionId);
	}

}
