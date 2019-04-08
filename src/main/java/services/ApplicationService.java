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
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;

import domain.Hacker;
import domain.Problem;
import org.springframework.util.Assert;

import repositories.ApplicationRepository;
import domain.Application;
import domain.Company;
import domain.Curriculum;
import domain.Position;
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
			Curriculum copyCur = this.curriculumService.copyCurriculum(application.getCurriculum());
			result = this.createApplication();
			List<Problem> problems = application.getPosition().getProblems();
			Random rand = new Random();
			Problem p = problems.get(rand.nextInt(problems.size()));
			result.setProblem(p);
			result.setCurriculum(copyCur);
			result.setPosition(application.getPosition());
		} else {
			Application copy = this.findOne(application.getId());

			result.setVersion(copy.getVersion());
			result.setCreationMoment(copy.getCreationMoment());

			if (isUrl(application.getLink()) || application.getLink() == "")
				result.setLink(application.getLink());
			else {
				binding.rejectValue("link", "Isn't an URL");
			}
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
		this.positionService.save(position);
		this.companyService.save(loggedCompany);

	}

	public List<Application> getSubmittedApplicationCompany(Integer positionId) {
		return this.applicationRepository.getSubmittedApplicationsCompany(positionId);
	}
}
