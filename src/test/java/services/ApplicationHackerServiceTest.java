package services;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Application;
import domain.Hacker;
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

	@Test
	public void driverListApplication() {

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
			
			List<Application> allApplications = hacker.getApplications();
			List<Application> filterApplications = (List<Application>) this.applicationService.getApplicationsByHackerAndStatus(hacker, res);
			
			if (status.contentEquals(""))
				Assert.isTrue(allApplications.size() == filterApplications.size());
			else
				Assert.isTrue(allApplications.size() == (filterApplications.size() + (allApplications.size() - filterApplications.size())));
			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.rollbackTransaction();
		}
		
		super.checkExceptions(expected, caught);
	}

}
