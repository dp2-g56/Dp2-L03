
package services;

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
public class DeleteMessageServiceTest extends AbstractTest {

	@Autowired
	private MessageService	messageService;
	@Autowired
	private ActorService	actorService;


	/*
	 * Positive test + asserts = 3
	 * Total test = 4
	 * Total coverage of the problem = 100%
	 */

	@Test
	public void driverDeleteMessage() {
		Object testingData[][] = {

			{
				//Positive test, Delete message you own
				"admin1", "message1", null
			}, {
				//Positive test, Delete message you own
				"admin1", "message2", null
			}, {
				//Negative test, not logged
				"", "message2", IllegalArgumentException.class
			}, {
				//Negative test, Delete a message is not yours
				"admin1", "message3", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDeleteMessage((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	protected void templateDeleteMessage(String username, String messageRe, Class<?> expected) {

		Class<?> caught = null;

		try {

			//En cada iteraccion comenzamos una transaccion, de esya manera, no se toman valores residuales de otros test
			this.startTransaction();

			super.authenticate(username);

			Message message = this.messageService.findOne(super.getEntityId(messageRe));

			this.messageService.deleteMessage(message);

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
