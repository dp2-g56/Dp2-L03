package services;

import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Application;
import domain.Curriculum;
import domain.Hacker;
import domain.Position;
import domain.Problem;
import domain.Status;
import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@Transactional
public class ApplicationHackerServiceTest extends AbstractTest {

	@Autowired
	private ApplicationService applicationService;

	@Autowired
	private HackerService hackerService;

	@Autowired
	private CurriculumService curriculumService;

	@Autowired
	private PositionService positionService;

	/*
	 * 10.An actor who is authenticated as a hacker must be able to:1.Manage his or
	 * her applications, which includes listing them grouped by status, showing
	 * them, creating them, and updating them.When an application is created, the
	 * system assigns an arbitrary problem to it (from the set of problems that have
	 * been registered for the corresponding position). Updating an application
	 * consists in submitting a solution to the corresponding problem (a piece of
	 * text with explanations and a link to the code), registering the submission
	 * moment, and changing the status to SUBMITTED
	 */

	// Ratio of data coverage: 6/6 = 100%
	@Test
	public void driverListApplication() {

		Object testingData[][] = {
				// Positive test: Listing all applications of the hacker
				{ "", "hacker1", null },
				// Positive test: Listing accepted applications of the hacker
				{ "ACCEPTED", "hacker1", null },
				// Positive test: Listing pending applications of the hacker
				{ "PENDING", "hacker1", null },
				// Positive test: Listing rejected applications of the hacker
				{ "REJECTED", "hacker1", null },
				// Positive test: Listing submitted applications of the hacker
				{ "SUBMITTED", "hacker1", null },
				// Negative test: Trying to list all applications with a different role
				{ "", "company1", IllegalArgumentException.class } };

		for (int i = 0; i < testingData.length; i++)
			this.templateListApplication((String) testingData[i][0], (String) testingData[i][1],
					(Class<?>) testingData[i][2]);
	}

	private void templateListApplication(String status, String username, Class<?> expected) {

		Class<?> caught = null;

		try {
			this.startTransaction();

			super.authenticate(username);

			Hacker hacker = this.hackerService.loggedHacker();

			Status res = null;

			if (status == Status.ACCEPTED.toString()) {
				res = Status.ACCEPTED;
			} else if (status == Status.PENDING.toString()) {
				res = Status.PENDING;
			} else if (status == Status.REJECTED.toString()) {
				res = Status.REJECTED;
			} else if (status == Status.SUBMITTED.toString()) {
				res = Status.SUBMITTED;
			} else {
				res = null;
			}

			if (hacker == null) {
				throw new IllegalArgumentException();
			}

			List<Application> allApplications = hacker.getApplications();
			List<Application> filterApplications = (List<Application>) this.applicationService
					.getApplicationsByHackerAndStatus(hacker, res);

			if (status.contentEquals(""))
				Assert.isTrue(allApplications.size() == hacker.getApplications().size());
			else
				Assert.isTrue(allApplications
						.size() == (filterApplications.size() + (allApplications.size() - filterApplications.size())));
			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.rollbackTransaction();
		}

		super.checkExceptions(expected, caught);
	}

	// Ratio of data coverage: 2/2 = 100%
	@Test
	public void driverCreateApplication() {

		Object testingData[][] = {
				// Positive test: Create an application as a hacker
				{ "hacker1", null },
				// Negative test: Trying to create an application with a different role
				{ "company1", IllegalArgumentException.class } };

		for (int i = 0; i < testingData.length; i++) {
			this.templateCreateApplication((String) testingData[i][0], (Class<?>) testingData[i][1]);
		}
	}

	private void templateCreateApplication(String username, Class<?> expected) {
		Class<?> caught = null;

		try {
			this.startTransaction();

			super.authenticate(username);

			Application application = this.applicationService.createApplication();
			application.setPosition(this.positionService.findAll().get(0));

			Curriculum copyCur = this.curriculumService.findAll().get(0);
			List<Problem> problems = application.getPosition().getProblems();
			Random rand = new Random();
			Problem p = problems.get(rand.nextInt(problems.size()));
			application.setProblem(p);
			application.setCurriculum(copyCur);

			this.applicationService.save(application);
			this.applicationService.flush();

			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.rollbackTransaction();
		}

		super.checkExceptions(expected, caught);

	}

	// Ratio of data coverage: 3/4 = 75%
	@Test
	public void driverUpdateApplication() {
		Application a = this.applicationService.findOne(super.getEntityId("application1"));

		Object testingData[][] = {
				// Positive test: Update an application as a hacker
				{ a, "http://www.solution.com", "explication1", "hacker1", null },
				// Negative test: Trying to update an application with an invalid URL
				{ a, "aaaa", "explication2", "hacker1", ConstraintViolationException.class },
				// Negative test: Trying to update an application with a different role
				{ a, "http://www.solution.com", "explication1", "admin1", IllegalArgumentException.class } };

		for (int i = 0; i < testingData.length; i++) {
			this.templateUpdateApplication((Application) testingData[i][0], (String) testingData[i][1],
					(String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
		}

	}

	private void templateUpdateApplication(Application application, String link, String explication, String username,
			Class<?> expected) {
		Class<?> caught = null;

		Date thisMoment = new Date();
		thisMoment.setTime(thisMoment.getTime() - 1);

		try {
			this.startTransaction();

			super.authenticate(username);

			Hacker hacker = this.hackerService.loggedHacker();

			if (hacker == null) {
				throw new IllegalArgumentException();
			}

			application.setExplication(explication);
			application.setSubmitMoment(thisMoment);
			application.setStatus(Status.SUBMITTED);
			application.setLink(link);

			this.applicationService.save(application);

			this.applicationService.flush();

			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.rollbackTransaction();
		}

		super.checkExceptions(expected, caught);

	}

}
