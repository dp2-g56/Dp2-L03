
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
import domain.Hacker;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class CreateHackerServiceTest extends AbstractTest {

	@Autowired
	private ActorService		actorService;

	@Autowired
	private HackerService		hackerService;

	@Autowired
	private CreditCardService	creditCardService;


	/*
	 * Positive test + asserts = 15
	 * Total test = 16
	 * Total coverage of the problem = 100%
	 */

	@Test
	public void driverCreateHacker() {
		Object testingData[][] = {

			{
				//All data is correct
				"name", "surname", "ATU00000024", "holderName", 4164810248953065L, 12, 25, 111, "VISA", "https://www.photo.com/", "email@gmail.com", "666555444", "address", "username", "password", null
			}, {
				//Negative test, Blank name
				"", "surname", "ATU00000024", "holderName", 4164810248953065L, 12, 25, 111, "VISA", "https://www.photo.com/", "email@gmail.com", "666555444", "address", "username", "password", ConstraintViolationException.class
			}, {
				//Negative test, Blank surname
				"name", "", "ATU00000024", "holderName", 4164810248953065L, 12, 25, 111, "VISA", "https://www.photo.com/", "email@gmail.com", "666555444", "address", "username", "password", ConstraintViolationException.class
			}, {
				//Negative test, invalid VAT number
				"name", "surname", "POEAU00000024", "holderName", 4164810248953065L, 12, 25, 111, "VISA", "https://www.photo.com/", "email@gmail.com", "666555444", "address", "username", "password", ConstraintViolationException.class
			}, {
				//Negative test, Blank VAT number
				"name", "surname", "", "holderName", 4164810248953065L, 12, 25, 111, "VISA", "https://www.photo.com/", "email@gmail.com", "666555444", "address", "username", "password", ConstraintViolationException.class
			}, {
				//Negative test, Blank Holder Name
				"name", "surname", "ATU00000024", "", 4164810248953065L, 12, 25, 111, "VISA", "https://www.photo.com/", "email@gmail.com", "666555444", "address", "username", "password", ConstraintViolationException.class
			}, {
				//Negative test, less than 16 digits credit card
				"name", "surname", "ATU00000024", "holderName", 41102465L, 12, 25, 111, "VISA", "https://www.photo.com/", "email@gmail.com", "666555444", "address", "username", "password", IllegalArgumentException.class
			}, {
				//Negative test, invalid credit card number
				"name", "surname", "ATU00000024", "holderName", 4164810248953000L, 12, 25, 111, "VISA", "https://www.photo.com/", "email@gmail.com", "666555444", "address", "username", "password", IllegalArgumentException.class
			}, {
				//Negative test, invalid Month
				"name", "surname", "ATU00000024", "holderName", 4164810248953065L, 99, 25, 111, "VISA", "https://www.photo.com/", "email@gmail.com", "666555444", "address", "username", "password", ConstraintViolationException.class
			}, {
				//Negative test, invalid Year
				"name", "surname", "ATU00000024", "holderName", 4164810248953065L, 12, 10, 111, "VISA", "https://www.photo.com/", "email@gmail.com", "666555444", "address", "username", "password", IllegalArgumentException.class
			}, {
				//Negative test, invalid CVV
				"name", "surname", "ATU00000024", "holderName", 4164810248953065L, 12, 20, 0, "VISA", "https://www.photo.com/", "email@gmail.com", "666555444", "address", "username", "password", ConstraintViolationException.class
			}, {
				//Negative test, Blank card type
				"name", "surname", "ATU00000024", "holderName", 4164810248953065L, 12, 20, 111, "", "https://www.photo.com/", "email@gmail.com", "666555444", "address", "username", "password", ConstraintViolationException.class
			}, {
				//Negative test, invalid photo format
				"name", "surname", "ATU00000024", "holderName", 4164810248953065L, 12, 20, 111, "VISA", "invalidPhoto", "email@gmail.com", "666555444", "address", "username", "password", ConstraintViolationException.class
			}, {
				//Negative test, Blank email
				"name", "surname", "ATU00000024", "holderName", 4164810248953065L, 12, 20, 111, "VISA", "https://www.photo.com/", "", "666555444", "address", "username", "password", ConstraintViolationException.class
			}, {
				//Negative test, Blank username
				"name", "surname", "ATU00000024", "holderName", 4164810248953065L, 12, 20, 111, "VISA", "https://www.photo.com/", "email@gmail.com", "666555444", "address", "", "password", ConstraintViolationException.class
			}, {
				//Negative test, Blank password
				"name", "surname", "ATU00000024", "holderName", 4164810248953065L, 12, 20, 111, "VISA", "https://www.photo.com/", "email@gmail.com", "666555444", "address", "username", "", ConstraintViolationException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateHacker((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Long) testingData[i][4], (Integer) testingData[i][5], (Integer) testingData[i][6],
				(Integer) testingData[i][7], (String) testingData[i][8], (String) testingData[i][9], (String) testingData[i][10], (String) testingData[i][11], (String) testingData[i][12], (String) testingData[i][13], (String) testingData[i][14],
				(Class<?>) testingData[i][15]);

	}

	private void templateCreateHacker(String name, String surname, String VAT, String holderName, Long number, Integer month, Integer year, Integer CVV, String cardType, String photo, String email, String phone, String address, String username,
		String password, Class<?> expected) {
		Class<?> caught = null;
		this.startTransaction();

		try {

			Hacker hacker = this.hackerService.createHacker();
			CreditCard creditCard = new CreditCard();

			creditCard.setBrandName(cardType);
			creditCard.setCvvCode(CVV);
			creditCard.setExpirationMonth(month);
			creditCard.setExpirationYear(year);
			creditCard.setHolderName(holderName);
			creditCard.setNumber(number);

			hacker.setName(name);
			hacker.setSurname(surname);
			hacker.setVATNumber(VAT);
			hacker.setCreditCard(creditCard);
			hacker.setPhoto(photo);
			hacker.setEmail(email);
			hacker.setPhone(phone);
			hacker.setAddress(address);
			hacker.getUserAccount().setUsername(username);
			hacker.getUserAccount().setPassword(password);

			Assert.isTrue(this.creditCardService.validateDateCreditCard(creditCard));
			Assert.isTrue(this.creditCardService.validateDateCreditCard(creditCard));
			Assert.isTrue(this.creditCardService.validateNumberCreditCard(creditCard));

			this.hackerService.save(hacker);
			this.hackerService.flush();

		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);

		this.rollbackTransaction();

	}

}
