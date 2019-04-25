
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class DeleteHackerServiceTest extends AbstractTest {

	@Autowired
	private HackerService	hackerService;


	/*
	 * Positive test + asserts = 2
	 * Total test = 2
	 * Total coverage of the problem = 100%
	 */

	@Test
	public void driverDeleteHacker() {

		Object testingData[][] = {

			{
				//Positive case, a hacker delete his user account
				"hacker1", null
			},

			{
				//Negative case, a company delete a hacker account
				"company1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.DeleteHackerTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);

	}

	private void DeleteHackerTemplate(String hacker, Class<?> expected) {

		Class<?> caught = null;

		try {
			super.startTransaction();
			super.authenticate(hacker);

			this.hackerService.deleteHacker();

			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.rollbackTransaction();
		}

		super.checkExceptions(expected, caught);

	}

}
