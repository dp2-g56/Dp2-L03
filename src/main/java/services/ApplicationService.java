
package services;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.ModelAndView;

import repositories.ApplicationRepository;
import domain.Actor;
import domain.Application;
import domain.Company;
import domain.Curriculum;
import domain.Hacker;
import domain.Position;
import domain.Problem;
import domain.Status;

@Service
@Transactional
public class ApplicationService {

	@Autowired
	private ApplicationRepository applicationRepository;

	@Autowired
	private ProblemService problemService;

	@Autowired
	private HackerService hackerService;

	@Autowired
	private Validator validator;

	@Autowired
	private CompanyService companyService;

	@Autowired
	private PositionService positionService;

	@Autowired
	private CurriculumService curriculumService;

	@Autowired
	private MessageService messageService;

	public List<Application> findAll() {
		Hacker hacker = this.hackerService.loggedHacker();
		List<Application> applications = new ArrayList<Application>();
		applications = this.applicationRepository.findAll();
		return applications;
	}

	public Collection<Application> getApplicationsByHacker(Hacker hacker) {
		return this.applicationRepository.getApplicationsByHacker(hacker);
	}

	public Collection<Application> getApplicationsByHackerAndStatus(Hacker hacker, Status status) {
		return this.applicationRepository.getApplicationsByHackerAndStatus(hacker, status);
	}

	public Application createApplication() {
		this.hackerService.loggedAsHacker();
		Application application = new Application();

		Date thisMoment = new Date();
		thisMoment.setTime(thisMoment.getTime() - 1);

		application.setCreationMoment(thisMoment);
		application.setStatus(Status.PENDING);

		return application;

	}

	public Application reconstruct(Application application, BindingResult binding) {
		Application result = new Application();
		Date thisMoment = new Date();
		thisMoment.setTime(thisMoment.getTime() - 1);

		if (application.getId() == 0) {
			if (application.getPosition().getIsCancelled().equals(true)) {
				result = null;
			} else {
				Curriculum copyCur = this.curriculumService.copyCurriculum(application.getCurriculum());
				result = this.createApplication();
				List<Problem> problems = application.getPosition().getProblems();
				Random rand = new Random();
				Problem p = problems.get(rand.nextInt(problems.size()));
				result.setProblem(p);
				result.setCurriculum(copyCur);
				result.setPosition(application.getPosition());
			}
		} else {
			Application copy = this.findOne(application.getId());

			result.setVersion(copy.getVersion());
			result.setCreationMoment(copy.getCreationMoment());

			if (this.isUrl(application.getLink()) || application.getLink() == "")
				result.setLink(application.getLink());
			else
				binding.rejectValue("link", "Isn't an URL");
			result.setExplication(application.getExplication());
			result.setSubmitMoment(thisMoment);
			result.setStatus(Status.SUBMITTED);
			result.setProblem(copy.getProblem());
			result.setPosition(copy.getPosition());
			result.setCurriculum(copy.getCurriculum());
			result.setHacker(copy.getHacker());
			result.setId(copy.getId());
		}

		this.validator.validate(result, binding);
		return result;

	}

	public Boolean isUrl(String url) {
		try {
			new URL(url).toURI();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// ----------------------------------------CRUD
	// METHODS--------------------------
	// ------------------------------------------------------------------------------
	public List<Application> getApplicationsCompany(int positionId) {
		return this.applicationRepository.getApplicationsCompany(positionId);
	}

	public Application findOne(int applicationId) {
		return this.applicationRepository.findOne(applicationId);
	}

	public Application findOneWithAssert(int applicationId) {
		Company loggedCompany = this.companyService.loggedCompany();
		Application application = this.applicationRepository.findOne(applicationId);
		Position position = application.getPosition();
		Assert.isTrue(loggedCompany.getPositions().contains(position));
		return application;
	}

	public Application save(Application application) {
		return this.applicationRepository.save(application);
	}

	// ---------------------------------EDIT AS
	// COMPANY-------------------------------
	// -------------------------------------------------------------------------------
	public void editApplicationCompany(Application application, boolean accept) {
		// Security
		Company loggedCompany = this.companyService.loggedCompany();
		List<Position> positions = loggedCompany.getPositions();
		Position position = application.getPosition();

		Assert.isTrue(positions.contains(position));
		Assert.isTrue(application.getStatus() == Status.SUBMITTED);

		// position.getApplications().remove(application);
		// positions.remove(position);
		//
		// position.setIsCancelled(true);
		// Position saved = this.save(position);
		// positions.add(saved);
		// loggedCompany.setPositions(positions);

		if (accept)
			application.setStatus(Status.ACCEPTED);
		else
			application.setStatus(Status.REJECTED);

		Application saved = this.applicationRepository.save(application);
		position.getApplications().remove(application);
		position.getApplications().add(saved);

		if (accept)
			this.messageService.notificationStatusApplicationAccepted(saved);
		else
			this.messageService.notificationStatusApplicationRejected(saved);

		this.positionService.save(position);
		this.companyService.save(loggedCompany);

	}

	public List<Application> getSubmittedApplicationCompany(Integer positionId) {
		return this.applicationRepository.getSubmittedApplicationsCompany(positionId);
	}

	public void deleteAllApplication() {

		Hacker hacker = new Hacker();

		hacker = this.hackerService.loggedHacker();

		List<Application> applications = new ArrayList<Application>();
		applications = hacker.getApplications();

		// Quitamos todos los applications de hacker

		List<Curriculum> curriculumsToDelete = this.applicationRepository.getCopyCurriculumsOfApplications(hacker);

		for (Application app : applications) {
			Position pos = new Position();
			pos = app.getPosition();
			pos.getApplications().remove(app);
			// app.setHacker(null);
			// app.setPosition(null);
		}

		// hacker.getApplications().removeAll(applications);

		/*
		 * List<Position> allPositionsOfHacker = new ArrayList<Position>();
		 * 
		 * allPositionsOfHacker =
		 * this.positionService.positionsOfApplicationOfHacker(hacker);
		 * 
		 * for (Position p : allPositionsOfHacker) if
		 * (Collections.disjoint(p.getApplications(), applications)) {
		 * 
		 * }
		 */

		this.applicationRepository.deleteInBatch(applications);
		this.curriculumService.deleteInBatch(curriculumsToDelete);

	}

	public void delete(Application application) {
		this.applicationRepository.delete(application);
	}

	public void deleteinBatch(List<Application> applications) {
		this.applicationRepository.deleteInBatch(applications);
	}

	public void flush() {
		this.applicationRepository.flush();
	}

	public Actor getCompanyByApplicationId(int applicationId) {
		return this.applicationRepository.getCompanyByApplicationId(applicationId);
	}
}
