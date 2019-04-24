
package services;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.AdminRepository;
import utilities.AbstractTest;
import domain.Actor;
import domain.Admin;
import domain.Company;
import domain.CreditCard;
import domain.Hacker;
import domain.Position;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class AdminServiceTest extends AbstractTest {

	@Autowired
	private AdminService			adminService;

	@Autowired
	private AdminRepository			adminRepository;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private CreditCardService		creditCardService;


	/**
	 * We are going to test Requirement 24.3
	 * 
	 * 24. An actor who is authenticated as an administrator must be able to:
	 * 
	 * 3. Ban an actor with the spammer flag.
	 * 
	 */

	@Test
	public void driverBan() {
		Object testingData[][] = {
			/**
			 * Positive test: an admin bans an actor with the spammer flag
			 */
			{
				"hacker1", "admin1", null
			},
			/**
			 * Negative test: an actor that is not authenticated as an admin
			 * tries to ban an user and an IllegalArgumentException is thrown
			 */
			{
				"hacker1", "hacker2", IllegalArgumentException.class
			},
			/**
			 * Negative test: an admin tries to ban an actor that is not suspicious
			 */
			{
				"hacker2", "admin1", IllegalArgumentException.class
			}
		};

		/**
		 * Data coverage:
		 * 
		 * 3/3
		 * 
		 */

		for (int i = 0; i < testingData.length; i++) {
			this.templateBan((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
		}
	}

	private void templateBan(String user, String username, Class<?> expected) {
		Class<?> caught = null;

		try {
			this.startTransaction();

			super.authenticate(username);

			Actor usern = this.actorService.getActorByUsername(user);
			if (usern.getHasSpam()) {
				this.adminService.banSuspiciousActor(usern);
			} else {
				throw new IllegalArgumentException();
			}

			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.rollbackTransaction();
		}

		super.checkExceptions(expected, caught);

	}

	/**
	 * We are going to test Requirement 24.4
	 * 
	 * 24. An actor who is authenticated as an administrator must be able to:
	 * 
	 * 4. Unban an actor who was banned previously.
	 * 
	 */

	@Test
	public void driverUnBan() {
		Object testingData[][] = {
			/**
			 * Positivce case: an actor logged as an admin unbans an actor
			 */
			{
				"hacker1", "admin1", null
			},
			/**
			 * Negative case: an actor that is not an admin tries to unban an actor
			 */
			{
				"hacker1", "hacker2", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			this.templateUnBan((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
		}
	}

	private void templateUnBan(String user, String username, Class<?> expected) {
		Class<?> caught = null;

		try {
			this.startTransaction();
			super.authenticate(username);

			//First, we ban the actor
			Actor usern = this.actorService.getActorByUsername(user);
			usern.getUserAccount().setIsNotLocked(false);
			this.actorService.save(usern);

			//Now, we try to unban the actor
			this.adminService.unBanSuspiciousActor(usern);

			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.rollbackTransaction();
		}

		super.checkExceptions(expected, caught);

	}

	@Test
	public void driverCreateAdmin() {
		Object testingData[][] = {

			{
				//All data is correct
				"name", "surname", "ATU00000024", "holderName", 4164810248953065L, 12, 25, 111, "VISA", "https://www.photo.com/", "email@gmail.com", "666555444", "address", "username", "password", "admin1", null
			}, {
				//Negative test, Blank name
				"", "surname", "ATU00000024", "holderName", 4164810248953065L, 12, 25, 111, "VISA", "https://www.photo.com/", "email@gmail.com", "666555444", "address", "username", "password", "admin1", ConstraintViolationException.class
			}, {
				//Negative test, Blank surname
				"name", "", "ATU00000024", "holderName", 4164810248953065L, 12, 25, 111, "VISA", "https://www.photo.com/", "email@gmail.com", "666555444", "address", "username", "password", "admin1", ConstraintViolationException.class
			}, {
				//Negative test, invalid VAT number
				"name", "surname", "POEAU00000024", "holderName", 4164810248953065L, 12, 25, 111, "VISA", "https://www.photo.com/", "email@gmail.com", "666555444", "address", "username", "password", "admin1", ConstraintViolationException.class
			}, {
				//Negative test, Blank VAT number
				"name", "surname", "", "holderName", 4164810248953065L, 12, 25, 111, "VISA", "https://www.photo.com/", "email@gmail.com", "666555444", "address", "username", "password", "admin1", ConstraintViolationException.class
			}, {
				//Negative test, Blank Holder Name
				"name", "surname", "ATU00000024", "", 4164810248953065L, 12, 25, 111, "VISA", "https://www.photo.com/", "email@gmail.com", "666555444", "address", "username", "password", "admin1", ConstraintViolationException.class
			}, {
				//Negative test, less than 16 digits credit card
				"name", "surname", "ATU00000024", "holderName", 41102465L, 12, 25, 111, "VISA", "https://www.photo.com/", "email@gmail.com", "666555444", "address", "username", "password", "admin1", IllegalArgumentException.class
			}, {
				//Negative test, invalid credit card number
				"name", "surname", "ATU00000024", "holderName", 4164810248953000L, 12, 25, 111, "VISA", "https://www.photo.com/", "email@gmail.com", "666555444", "address", "username", "password", "admin1", IllegalArgumentException.class
			}, {
				//Negative test, invalid Month
				"name", "surname", "ATU00000024", "holderName", 4164810248953065L, 99, 25, 111, "VISA", "https://www.photo.com/", "email@gmail.com", "666555444", "address", "username", "password", "admin1", ConstraintViolationException.class
			}, {
				//Negative test, invalid Year
				"name", "surname", "ATU00000024", "holderName", 4164810248953065L, 12, 10, 111, "VISA", "https://www.photo.com/", "email@gmail.com", "666555444", "address", "username", "password", "admin1", IllegalArgumentException.class
			}, {
				//Negative test, invalid CVV
				"name", "surname", "ATU00000024", "holderName", 4164810248953065L, 12, 20, 0, "VISA", "https://www.photo.com/", "email@gmail.com", "666555444", "address", "username", "password", "admin1", ConstraintViolationException.class
			}, {
				//Negative test, Blank card type
				"name", "surname", "ATU00000024", "holderName", 4164810248953065L, 12, 20, 111, "", "https://www.photo.com/", "email@gmail.com", "666555444", "address", "username", "password", "admin1", ConstraintViolationException.class
			}, {
				//Negative test, invalid photo format
				"name", "surname", "ATU00000024", "holderName", 4164810248953065L, 12, 20, 111, "VISA", "invalidPhoto", "email@gmail.com", "666555444", "address", "username", "password", "admin1", ConstraintViolationException.class
			}, {
				//Negative test, Blank email
				"name", "surname", "ATU00000024", "holderName", 4164810248953065L, 12, 20, 111, "VISA", "https://www.photo.com/", "", "666555444", "address", "username", "password", "admin1", ConstraintViolationException.class
			}, {
				//Negative test, Blank username
				"name", "surname", "ATU00000024", "holderName", 4164810248953065L, 12, 20, 111, "VISA", "https://www.photo.com/", "email@gmail.com", "666555444", "address", "", "password", "admin1", ConstraintViolationException.class
			}, {
				//Negative test, Blank password
				"name", "surname", "ATU00000024", "holderName", 4164810248953065L, 12, 20, 111, "VISA", "https://www.photo.com/", "email@gmail.com", "666555444", "address", "username", "", "admin1", ConstraintViolationException.class
			}, {
				//Negative test, Blank adminNumber
				"name", "surname", "ATU00000024", "holderName", 4164810248953065L, 12, 20, 111, "VISA", "https://www.photo.com/", "email@gmail.com", "666555444", "address", "username", "password", "hacker1", IllegalArgumentException.class
			},
		};

		for (int i = 0; i < testingData.length; i++) {
			this.templateCreateAdmin((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Long) testingData[i][4], (Integer) testingData[i][5], (Integer) testingData[i][6],
				(Integer) testingData[i][7], (String) testingData[i][8], (String) testingData[i][9], (String) testingData[i][10], (String) testingData[i][11], (String) testingData[i][12], (String) testingData[i][13], (String) testingData[i][14],
				(String) testingData[i][15], (Class<?>) testingData[i][16]);
		}

	}

	private void templateCreateAdmin(String name, String surname, String VAT, String holderName, Long number, Integer month, Integer year, Integer CVV, String cardType, String photo, String email, String phone, String address, String username,
		String password, String adminName, Class<?> expected) {
		Class<?> caught = null;
		this.startTransaction();
		this.authenticate(adminName);

		try {

			Admin admin = this.adminService.createAdmin();
			CreditCard creditCard = new CreditCard();

			creditCard.setBrandName(cardType);
			creditCard.setCvvCode(CVV);
			creditCard.setExpirationMonth(month);
			creditCard.setExpirationYear(year);
			creditCard.setHolderName(holderName);
			creditCard.setNumber(number);

			admin.setName(name);
			admin.setSurname(surname);
			admin.setVATNumber(VAT);
			admin.setCreditCard(creditCard);
			admin.setPhoto(photo);
			admin.setEmail(email);
			admin.setPhone(phone);
			admin.setAddress(address);
			admin.getUserAccount().setUsername(username);
			admin.getUserAccount().setPassword(password);

			Assert.isTrue(this.creditCardService.validateDateCreditCard(creditCard));
			Assert.isTrue(this.creditCardService.validateDateCreditCard(creditCard));
			Assert.isTrue(this.creditCardService.validateNumberCreditCard(creditCard));

			this.adminService.saveNewAdmin(admin);
			this.adminService.flush();

		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
		this.unauthenticate();
		this.rollbackTransaction();

	}

	@Test
	public void testAvgCurriculumPerHacker() {
		Float query = this.adminRepository.avgCurriculumPerHacker();
		Assert.isTrue(query > 0.1);
	}

	@Test
	public void testMinCurriculumPerHacker() {
		Float query = this.adminRepository.minCurriculumPerHacker();
		Assert.isTrue(query == 0.0);
	}

	@Test
	public void testMaxCurriculumPerHacker() {
		Float query = this.adminRepository.maxCurriculumPerHacker();
		Assert.isTrue(query == 2.0);
	}

	@Test
	public void testStddevCurriculumPerHacker() {
		Float query = this.adminRepository.stddevCurriculumPerHacker();
		Assert.isTrue(query < 0.5 && query > 0.3);
	}

	public Date dateToFinder() {
		Date thisMoment = new Date();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(thisMoment);
		calendar.add(Calendar.HOUR_OF_DAY, -this.configurationService.getConfiguration().getTimeFinder());

		return calendar.getTime();
	}

	@Test
	public void testAvgResultFinders() {

		Float query = this.adminRepository.avgResultFinders(this.dateToFinder());
		Assert.isTrue(query == null);
	}

	@Test
	public void testMinResultFinders() {
		Float query = this.adminRepository.minResultFinders(this.dateToFinder());
		Assert.isTrue(query == null);
	}

	@Test
	public void testMaxResultFinders() {
		Float query = this.adminRepository.maxResultFinders(this.dateToFinder());
		Assert.isTrue(query == null);
	}

	@Test
	public void testStddevResultFinders() {
		Float query = this.adminRepository.stddevResultFinders(this.dateToFinder());
		Assert.isTrue(query == null);
	}

	@Test
	public void testRatioEmptyFinder() {
		Float query = this.adminRepository.ratioEmptyFinder(this.dateToFinder());
		Assert.isTrue(query == null);
	}

	@Test
	public void testAvgPositionsCompany() {

		Float query = this.adminRepository.avgPositionsCompany();
		Assert.isTrue(query == 2.0);
	}

	@Test
	public void testMinPositionsCompany() {
		Float query = this.adminRepository.minPositionsCompany();
		Assert.isTrue(query == 0.0);
	}

	@Test
	public void testMaxPositionsCompany() {
		Float query = this.adminRepository.maxPositionsCompany();
		Assert.isTrue(query == 4.0);
	}

	@Test
	public void testStddevPositionsCompany() {
		Float query = this.adminRepository.stddevPositionsCompany();
		Assert.isTrue(query < 1.4142 && query > 1.4141);
	}

	@Test
	public void testAvgApplicationsHacker() {

		Float query = this.adminRepository.avgApplicationsHacker();
		Assert.isTrue(query > 0.0033);
	}

	@Test
	public void testMinApplicationsHacker() {
		Float query = this.adminRepository.minApplicationsHacker();
		Assert.isTrue(query == 0.0);
	}

	@Test
	public void testMaxApplicationsHacker() {
		Float query = this.adminRepository.maxApplicationsHacker();
		Assert.isTrue(query == 1.0);
	}

	@Test
	public void testStddevApplicationsHacker() {
		Float query = this.adminRepository.stddevApplicationsHacker();
		Assert.isTrue(query < 0.2 && query > 0.1);
	}

	@Test
	public void testAvgSalaries() {

		Float query = this.adminRepository.avgSalaries();
		Assert.isTrue(query == 982.5);
	}

	@Test
	public void testMinSalary() {
		Float query = this.adminRepository.minSalary();
		Assert.isTrue(query == 3.0);
	}

	@Test
	public void testMaxSalary() {
		Float query = this.adminRepository.maxSalary();
		Assert.isTrue(query == 3240.0);
	}

	@Test
	public void testStddevSalaries() {
		Float query = this.adminRepository.stddevSalaries();
		Assert.isTrue(query > 1357.0804 && query < 1357.0805);
	}

	@Test
	public void testCompaniesMorePositions() {
		List<Company> query = this.adminRepository.companiesMorePositions();
		Assert.isTrue(query.size() == 1);

	}

	@Test
	public void testHackersMoreApplications() {
		List<Hacker> query = this.adminRepository.hackersMoreApplications();
		Assert.isTrue(query.size() == 1);

	}

	@Test
	public void testBestSalaryPositions() {
		List<Position> query = this.adminRepository.bestSalaryPositions();
		Assert.isTrue(query.size() == 2);

	}

	@Test
	public void testWorstSalaryPositions() {
		List<Position> query = this.adminRepository.worstSalaryPositions();
		Assert.isTrue(query.size() == 3);

	}

}
