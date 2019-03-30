
package services;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.Problem;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class CompanyTest extends AbstractTest {

	@Autowired
	private ProblemService	problemService;
	@Autowired
	private CompanyService	companyService;


	@Test
	public void driverEditProblem() {
		Object testingData[][] = {

			{
				"company1", "problem6", "title1", "statement1", null
			}, {
				"company1", "problem6", "", "statement1", ConstraintViolationException.class
			}, {
				"company1", "problem6", "title1", "", ConstraintViolationException.class
			}, {
				"company1", "problem1", "title1", "statement1", IllegalArgumentException.class
			}, {
				"company2", "problem6", "title1", "statement1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			this.templateEditProblem((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
		}

	}

	@Test
	public void driverCreateProblem() {
		Object testingData[][] = {

			{
				"company1", "title1", "statement1", null
			}, {
				"company1", "", "statement1", ConstraintViolationException.class
			}, {
				"company1", "title1", "", ConstraintViolationException.class
			}, {
				"hacker1", "title1", "statement1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			this.templateCreateProblem((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
		}

	}

	@Test
	public void driverDeleteProblem() {
		Object testingData[][] = {

			{
				"company1", "problem6", null
			}, {
				"company1", "problem3", IllegalArgumentException.class
			}, {
				"company2", "problem6", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			this.templateDeleteProblem((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
		}

	}

	@Test
	public void driverDeleteAttachment() {
		Object testingData[][] = {

			{
				"company1", "problem6", 0, null
			}, {
				"company2", "problem6", 0, IllegalArgumentException.class
			}, {
				"company1", "problem3", 0, IllegalArgumentException.class
			}, {
				"company1", "problem6", 1, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			this.templateDeleteAttachment((String) testingData[i][0], (String) testingData[i][1], (Integer) testingData[i][2], (Class<?>) testingData[i][3]);
		}

	}

	@Test
	public void driverCreateAttachment() {
		Object testingData[][] = {

			{
				"company1", "problem6", "https://www.google.com/", null
			}, {
				"company1", "problem3", "https://www.google.com/", IllegalArgumentException.class
			}, {
				"company2", "problem6", "https://www.google.com/", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			this.templateCreateAttachment((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
		}

	}

	protected void templateEditProblem(String username, String problem, String title, String statement, Class<?> expected) {

		this.startTransaction();
		super.authenticate(username);

		Problem pro = this.problemService.findOne(super.getEntityId(problem));

		Problem proCopy = this.problemService.create();

		proCopy.setTitle(title);
		proCopy.setStatement(statement);
		proCopy.setIsDraftMode(pro.getIsDraftMode());
		proCopy.setVersion(pro.getVersion());
		proCopy.setId(pro.getId());

		Class<?> caught = null;

		try {
			this.problemService.updateProblem(proCopy);
			this.problemService.flush();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
		super.unauthenticate();

		this.rollbackTransaction();

	}

	protected void templateCreateProblem(String username, String title, String statement, Class<?> expected) {

		this.startTransaction();
		super.authenticate(username);

		Problem proCopy = this.problemService.create();

		proCopy.setTitle(title);
		proCopy.setStatement(statement);

		Class<?> caught = null;

		try {
			this.companyService.addProblem(proCopy);
			this.problemService.flush();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
		super.unauthenticate();

		this.rollbackTransaction();

	}

	protected void templateDeleteProblem(String username, String problem, Class<?> expected) {

		this.startTransaction();
		super.authenticate(username);

		Problem pro = this.problemService.findOne(super.getEntityId(problem));

		Class<?> caught = null;

		try {
			this.problemService.deleteProblem(pro);
			this.problemService.flush();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
		super.unauthenticate();

		this.rollbackTransaction();

	}

	protected void templateDeleteAttachment(String username, String problem, Integer index, Class<?> expected) {

		this.startTransaction();
		super.authenticate(username);

		Problem pro = this.problemService.findOne(super.getEntityId(problem));

		Class<?> caught = null;

		try {
			this.problemService.removeAttachment(pro, index);
			this.problemService.flush();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
		super.unauthenticate();

		this.rollbackTransaction();

	}

	protected void templateCreateAttachment(String username, String problem, String attachment, Class<?> expected) {

		this.startTransaction();
		super.authenticate(username);

		Problem pro = this.problemService.findOne(super.getEntityId(problem));

		Class<?> caught = null;

		try {
			this.problemService.addAttachment(attachment, pro);
			this.problemService.flush();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
		super.unauthenticate();

		this.rollbackTransaction();

	}
}
