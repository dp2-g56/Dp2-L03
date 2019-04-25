
package services;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Actor;
import domain.Message;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class SpamMessageServiceTest extends AbstractTest {

	@Autowired
	private MessageService	messageService;
	@Autowired
	private ActorService	actorService;


	/*
	 * Positive test + asserts = 2
	 * Total test = 4
	 * Total coverage of the problem = 100%
	 * We test each of the possibilities of spam words with asserts in the test method
	 */

	@Test
	public void driverSpamMessage() {
		Object testingData[][] = {

			{
				//Positive test, spam word in subject
				"admin1", "sex", "body", "tags", "hacker3", null
			}, {
				//Positive test, spam word in body
				"admin1", "subject", "sex", "", "hacker3", null
			}, {
				//Positive test, spam word in tag
				"admin1", "subject", "body", "sex", "hacker3", null
			}, {
				//Negative test, no spam words
				"admin1", "subject", "body", "tags", "hacker3", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateSpamMessage((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);

	}

	protected void templateSpamMessage(String username, String subject, String body, String tags, String receiver, Class<?> expected) {

		Class<?> caught = null;

		try {

			//En cada iteraccion comenzamos una transaccion, de esya manera, no se toman valores residuales de otros test
			this.startTransaction();

			super.authenticate(username);

			Actor actor = this.actorService.getActorByUsername(receiver);

			Actor sender = this.actorService.getActorByUsername(username);

			Date thisMoment = new Date();
			thisMoment.setTime(thisMoment.getTime() - 1000);

			Message message = this.messageService.create();

			message.setMoment(thisMoment);
			message.setSubject(subject);
			message.setBody(body);

			message.setReceiver(receiver);
			message.setTags(tags);

			Assert.isTrue(!sender.getHasSpam());

			this.messageService.sendMessage(message);
			this.messageService.flush();

			Assert.isTrue(actor.getMessages().get(0).getTags().contains("SPAM") && sender.getHasSpam());

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
