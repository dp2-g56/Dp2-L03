
package services;

import java.util.Date;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.Message;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class SendMessageServiceTest extends AbstractTest {

	@Autowired
	private MessageService	messageService;
	@Autowired
	private ActorService	actorService;


	/*
	 * Positive test + asserts + constraints = 4
	 * Total test = 5
	 * Total coverage of the problem = 100%
	 */

	@Test
	public void driverSendMessage() {
		Object testingData[][] = {

			{
				"admin1", "subject", "body", "tags", "hacker1", null
			}, {
				"admin1", "subject", "body", "", "hacker1", null
			}, {
				"", "subject", "body", "", "hacker1", IllegalArgumentException.class
			}, {
				"admin1", "", "body", "tags", "hacker1", ConstraintViolationException.class
			}, {
				"admin1", "subject", "", "tags", "hacker1", ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateSendMessage((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);

	}

	protected void templateSendMessage(String username, String subject, String body, String tags, String receiver, Class<?> expected) {

		Class<?> caught = null;

		try {

			//En cada iteraccion comenzamos una transaccion, de esya manera, no se toman valores residuales de otros test
			this.startTransaction();

			super.authenticate(username);

			Date thisMoment = new Date();
			thisMoment.setTime(thisMoment.getTime() - 1000);

			Message message = this.messageService.create();

			message.setMoment(thisMoment);
			message.setSubject(subject);
			message.setBody(body);

			message.setReceiver(receiver);
			message.setTags(tags);

			this.messageService.sendMessage(message);
			this.messageService.flush();

			super.authenticate(null);

		} catch (Throwable oops) {
			caught = oops.getClass();
		} finally {
			//Se fuerza el rollback para que no de ningun problema la siguiente iteracion
			this.rollbackTransaction();
		}

		super.checkExceptions(expected, caught);

	}

}
