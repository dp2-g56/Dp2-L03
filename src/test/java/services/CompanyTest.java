
package services;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Company;
import domain.CreditCard;
import domain.Problem;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class CompanyTest extends AbstractTest {

	@Autowired
	private ProblemService		problemService;
	@Autowired
	private CompanyService		companyService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private CreditCardService	creditCardService;


	/**
	 * We are going to test Requirement 9.2
	 * 
	 * 24. An actor who is authenticated as an company must be able to:...
	 */

	/*
	 * Positive test + Constrains + asserts = 4
	 * Total test = 5
	 * Total coverage of the problem = 100%
	 */
	@Test
	public void driverEditProblem() {
		Object testingData[][] = {

			{
				//Positive test, Edit a problem with correct data
				"company1", "problem6", "title1", "statement1", null
			}, {
				//Negative test, Edit a problem with blank tittle
				"company1", "problem6", "", "statement1", ConstraintViolationException.class
			}, {
				//Nevative test, Edit a problem with blank statement
				"company1", "problem6", "title1", "", ConstraintViolationException.class
			}, {
				//Negative test, Edit a problem that is not yours
				"company1", "problem1", "title1", "statement1", IllegalArgumentException.class
			}, {
				//Negative test, Edit a problem that is not yours
				"company2", "problem6", "title1", "statement1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEditProblem((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);

	}

	/*
	 * Positive test + Constrains + asserts = 4
	 * Total test = 5
	 * Total coverage of the problem = 100%
	 */
	@Test
	public void driverCreateProblem() {
		Object testingData[][] = {

			{
				//Positive test, create a problem with correct data
				"company1", "title1", "statement1", null
			}, {
				//Negative test, Blank title
				"company1", "", "statement1", ConstraintViolationException.class
			}, {
				//Negative test, Blank statement
				"company1", "title1", "", ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateProblem((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);

	}

	/*
	 * Positive test + asserts = 2
	 * Total test = 3
	 * Total coverage of the problem = 100%
	 */
	@Test
	public void driverDeleteProblem() {
		Object testingData[][] = {

			{
				//Positive test, deleting a problem you own
				"company1", "problem6", null
			}, {
				//Negative test, deleting a problem you don't own
				"company1", "problem3", IllegalArgumentException.class
			}, {
				//Negative test, deleting a problem you don't own
				"company2", "problem6", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDeleteProblem((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	/*
	 * Positive test + asserts = 2
	 * Total test = 4
	 * Total coverage of the problem = 100%
	 */

	@Test
	public void driverDeleteAttachment() {
		Object testingData[][] = {

			{
				//Positive test, Delete attachment 
				"company1", "problem6", 0, null
			}, {
				//Negative test, Delete an attachment that dont belongs to the problem
				"company2", "problem6", 0, IllegalArgumentException.class
			}, {
				//Negative test, Delete an attachment that dont belongs to the problem
				"company1", "problem3", 0, IllegalArgumentException.class
			}, {
				//Negative test, Delete an attachment that dont belongs to the problem
				"company1", "problem6", 1, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDeleteAttachment((String) testingData[i][0], (String) testingData[i][1], (Integer) testingData[i][2], (Class<?>) testingData[i][3]);

	}

	/*
	 * Positive test + asserts = 2
	 * Total test = 3
	 * Total coverage of the problem = 100%
	 */

	@Test
	public void driverCreateAttachment() {
		Object testingData[][] = {

			{
				//Positive test, create attachment with all data correct
				"company1", "problem6", "https://www.google.com/", null
			}, {
				//Negative test, create an attachment to a problem you dones't own
				"company1", "problem3", "https://www.google.com/", IllegalArgumentException.class
			}, {
				//Negative test, create an attachment to a problem you dones't own
				"company2", "problem6", "https://www.google.com/", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateAttachment((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);

	}

	/**
	 * On this test we are going to test the requirement 8.
	 * 
	 * An actor who is authenticated must be able to:
	 * 2. Edit his or her personal data.
	 * Ratio of data coverage: 2/3 = 66.67%
	 **/

	@Test
	public void editPersonalData() {
		Object testingData[][] = {

			{
				//POSITIVE: A company is editing his company name
				"company1", "company1", "companyName", null
			}, {
				//NEGATIVE: A company is trying to edit another company's name
				"company2", "company1", "companyName", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEditPersonalData((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);

	}

	private void templateEditPersonalData(String username, String companyToEdit, String name, Class<?> expected) {

		this.startTransaction();
		super.authenticate(username);

		Company company = this.companyService.findOne(this.getEntityId(companyToEdit));

		Class<?> caught = null;

		try {
			company.setCompanyName(name);
			this.companyService.updateCompany(company);
			this.problemService.flush();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
		super.unauthenticate();

		this.rollbackTransaction();

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

	@Test
	public void driverDeleteCompany() {

		Object testingData[][] = {
			// Positive case
			{
				"company1", null
			}, {
				"hacker1", IllegalArgumentException.class
			}
		// Negative case: Trying to delete an user with a different role

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDeleteMember((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	private void templateDeleteMember(String username, Class<?> expected) {

		Class<?> caught = null;

		try {
			this.startTransaction();
			super.authenticate(username);

			Assert.notNull(this.actorService.getActorByUsername(username));

			this.companyService.deleteCompany();

			Assert.isNull(this.actorService.getActorByUsername(username));

			super.unauthenticate();
			this.companyService.flush();
		} catch (Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.rollbackTransaction();
		}

		super.checkExceptions(expected, caught);

	}

	/*
	 * Positive test + asserts + constrains + credit card asserts = 16
	 * Total test = 17
	 * Total coverage of the problem = 100%
	 */

	@Test
	public void driverCreateCompany() {
		Object testingData[][] = {

			{
				//All data is correct
				"name", "surname", "ATU00000024", "holderName", 4164810248953065L, 12, 25, 111, "VISA", "https://www.photo.com/", "email@gmail.com", "666555444", "address", "username", "password", "com1", null
			}, {
				//Negative test, Blank name
				"", "surname", "ATU00000024", "holderName", 4164810248953065L, 12, 25, 111, "VISA", "https://www.photo.com/", "email@gmail.com", "666555444", "address", "username", "password", "com1", ConstraintViolationException.class
			}, {
				//Negative test, Blank surname
				"name", "", "ATU00000024", "holderName", 4164810248953065L, 12, 25, 111, "VISA", "https://www.photo.com/", "email@gmail.com", "666555444", "address", "username", "password", "com1", ConstraintViolationException.class
			}, {
				//Negative test, invalid VAT number
				"name", "surname", "POEAU00000024", "holderName", 4164810248953065L, 12, 25, 111, "VISA", "https://www.photo.com/", "email@gmail.com", "666555444", "address", "username", "password", "com1", ConstraintViolationException.class
			}, {
				//Negative test, Blank VAT number
				"name", "surname", "", "holderName", 4164810248953065L, 12, 25, 111, "VISA", "https://www.photo.com/", "email@gmail.com", "666555444", "address", "username", "password", "com1", ConstraintViolationException.class
			}, {
				//Negative test, Blank Holder Name
				"name", "surname", "ATU00000024", "", 4164810248953065L, 12, 25, 111, "VISA", "https://www.photo.com/", "email@gmail.com", "666555444", "address", "username", "password", "com1", ConstraintViolationException.class
			}, {
				//Negative test, less than 16 digits credit card
				"name", "surname", "ATU00000024", "holderName", 41102465L, 12, 25, 111, "VISA", "https://www.photo.com/", "email@gmail.com", "666555444", "address", "username", "password", "com1", IllegalArgumentException.class
			}, {
				//Negative test, invalid credit card number
				"name", "surname", "ATU00000024", "holderName", 4164810248953000L, 12, 25, 111, "VISA", "https://www.photo.com/", "email@gmail.com", "666555444", "address", "username", "password", "com1", IllegalArgumentException.class
			}, {
				//Negative test, invalid Month
				"name", "surname", "ATU00000024", "holderName", 4164810248953065L, 99, 25, 111, "VISA", "https://www.photo.com/", "email@gmail.com", "666555444", "address", "username", "password", "com1", ConstraintViolationException.class
			}, {
				//Negative test, invalid Year
				"name", "surname", "ATU00000024", "holderName", 4164810248953065L, 12, 10, 111, "VISA", "https://www.photo.com/", "email@gmail.com", "666555444", "address", "username", "password", "com1", IllegalArgumentException.class
			}, {
				//Negative test, invalid CVV
				"name", "surname", "ATU00000024", "holderName", 4164810248953065L, 12, 20, 0, "VISA", "https://www.photo.com/", "email@gmail.com", "666555444", "address", "username", "password", "com1", ConstraintViolationException.class
			}, {
				//Negative test, Blank card type
				"name", "surname", "ATU00000024", "holderName", 4164810248953065L, 12, 20, 111, "", "https://www.photo.com/", "email@gmail.com", "666555444", "address", "username", "password", "com1", ConstraintViolationException.class
			}, {
				//Negative test, invalid photo format
				"name", "surname", "ATU00000024", "holderName", 4164810248953065L, 12, 20, 111, "VISA", "invalidPhoto", "email@gmail.com", "666555444", "address", "username", "password", "com1", ConstraintViolationException.class
			}, {
				//Negative test, Blank email
				"name", "surname", "ATU00000024", "holderName", 4164810248953065L, 12, 20, 111, "VISA", "https://www.photo.com/", "", "666555444", "address", "username", "password", "com1", ConstraintViolationException.class
			}, {
				//Negative test, Blank username
				"name", "surname", "ATU00000024", "holderName", 4164810248953065L, 12, 20, 111, "VISA", "https://www.photo.com/", "email@gmail.com", "666555444", "address", "", "password", "com1", ConstraintViolationException.class
			}, {
				//Negative test, Blank password
				"name", "surname", "ATU00000024", "holderName", 4164810248953065L, 12, 20, 111, "VISA", "https://www.photo.com/", "email@gmail.com", "666555444", "address", "username", "", "com1", ConstraintViolationException.class
			}, {
				//Negative test, Blank companyNumber
				"name", "surname", "ATU00000024", "holderName", 4164810248953065L, 12, 20, 111, "VISA", "https://www.photo.com/", "email@gmail.com", "666555444", "address", "username", "password", "", ConstraintViolationException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateCompany((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Long) testingData[i][4], (Integer) testingData[i][5], (Integer) testingData[i][6],
				(Integer) testingData[i][7], (String) testingData[i][8], (String) testingData[i][9], (String) testingData[i][10], (String) testingData[i][11], (String) testingData[i][12], (String) testingData[i][13], (String) testingData[i][14],
				(String) testingData[i][15], (Class<?>) testingData[i][16]);

	}

	private void templateCreateCompany(String name, String surname, String VAT, String holderName, Long number, Integer month, Integer year, Integer CVV, String cardType, String photo, String email, String phone, String address, String username,
		String password, String companyName, Class<?> expected) {
		Class<?> caught = null;
		this.startTransaction();

		try {

			Company company = this.companyService.createCompany();
			CreditCard creditCard = new CreditCard();

			creditCard.setBrandName(cardType);
			creditCard.setCvvCode(CVV);
			creditCard.setExpirationMonth(month);
			creditCard.setExpirationYear(year);
			creditCard.setHolderName(holderName);
			creditCard.setNumber(number);

			company.setName(name);
			company.setSurname(surname);
			company.setVATNumber(VAT);
			company.setCreditCard(creditCard);
			company.setPhoto(photo);
			company.setEmail(email);
			company.setPhone(phone);
			company.setAddress(address);
			company.getUserAccount().setUsername(username);
			company.getUserAccount().setPassword(password);
			company.setCompanyName(companyName);

			Assert.isTrue(this.creditCardService.validateDateCreditCard(creditCard));
			Assert.isTrue(this.creditCardService.validateDateCreditCard(creditCard));
			Assert.isTrue(this.creditCardService.validateNumberCreditCard(creditCard));

			this.companyService.save(company);
			this.companyService.flush();

		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);

		this.rollbackTransaction();

	}

}
