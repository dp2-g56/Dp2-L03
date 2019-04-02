package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import domain.Application;
import domain.Hacker;
import domain.Problem;
import domain.Status;
import repositories.ApplicationRepository;

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

	public List<Application> findAll() {
		Hacker hacker = this.hackerService.loggedHacker();
		List<Application> applications = new ArrayList<Application>();
		applications = this.applicationRepository.findAll();
		return applications;
	}

	public Application findOne(int id) {
		Hacker hacker = this.hackerService.loggedHacker();
		Application application = this.applicationRepository.findOne(id);
		return application;
	}

	public Application save(Application a) {
		Hacker hacker = this.hackerService.loggedHacker();
		Application c = this.applicationRepository.save(a);
		return c;
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
		if (application.getCurriculum().getTitle() == "" || application.getPosition().getTitle() == "") {
			application.setCurriculum(null);
			application.setPosition(null);
		} else {

			if (application.getId() == 0) {
				result = this.createApplication();
				List<Problem> problems = application.getPosition().getProblems();
				Random rand = new Random();
				Problem p = problems.get(rand.nextInt(problems.size()));
				result.setProblem(p);
				result.setCurriculum(application.getCurriculum());
				result.setPosition(application.getPosition());
			} else {
				Application copy = this.findOne(application.getId());

				result.setVersion(copy.getVersion());
				result.setCreationMoment(copy.getCreationMoment());
				result.setLink(copy.getLink());
				result.setExplication(copy.getExplication());
				result.setSubmitMoment(copy.getSubmitMoment());
				result.setStatus(copy.getStatus());
				result.setProblem(copy.getProblem());
				result.setPosition(copy.getPosition());
				result.setCurriculum(application.getCurriculum());
				result.setHacker(copy.getHacker());
				result.setId(copy.getId());
			}
		}

		this.validator.validate(result, binding);
		return result;
	}

}
