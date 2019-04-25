
package services;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.Actor;
import domain.Message;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class BroadcastAdminServiceTest extends AbstractTest {

	@Autowired
	private MessageService	messageService;

	@Autowired
	private AdminService	adminService;

	@Autowired
	private ActorService	actorService;


	/**
	 * We are going to test Requirement 24.1
	 * 
	 * 24. An actor who is authenticated as an administrator must be able to:
	 * 
	 * 1. Broadcast a message to all actors
	 * 
	 */

	/**
	 * Coverage:
	 * In broadcastMessage, we have the positive case and the Assert to check that you are logged as admin
	 * The Message constrains are checked in SendMessageTest
	 * Positive tes + Constratins = 2
	 * Total test = 3
	 * Coverage = 3/2 = 1.5 = 150%
	 * */

	@Test
	public void driverUpdateMessage() {

		Object testingData[][] = {
			{
				//Positive test
				"admin1", "admin1", "subject", "body", null
			}, {
				//Negative test, broadcasting with a non admin actor
				"member1", "member1", "subject", "body", IllegalArgumentException.class
			}, {
				//Negative test, not logged actor
				"", "member1", "subject", "body", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateSendMessage((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
	}

	protected void templateSendMessage(String username, String usernameVerification, String subject, String body, Class<?> expected) {

		Class<?> caught = null;

		try {

			//En cada iteraccion comenzamos una transaccion, de esya manera, no se toman valores residuales de otros test
			this.startTransaction();

			super.authenticate(username);

			Date thisMoment = new Date();
			thisMoment.setTime(thisMoment.getTime() - 1000);

			Message message = this.messageService.create();
			Actor sender = this.actorService.getActorByUsername(usernameVerification);
			Actor receiverActor = this.actorService.getActorByUsername(usernameVerification);

			message.setMoment(thisMoment);
			message.setSubject(subject);
			message.setBody(body);
			message.setReceiver(receiverActor.getUserAccount().getUsername());

			message.setSender(sender.getUserAccount().getUsername());

			this.adminService.broadcastMessage(message);
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
