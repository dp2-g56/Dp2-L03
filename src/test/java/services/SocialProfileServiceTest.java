package services;

import java.util.List;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import domain.Actor;
import domain.SocialProfile;
import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class SocialProfileServiceTest  extends AbstractTest{
	
	@Autowired
	private SocialProfileService	socialProfileService;
	
	@Autowired
	private ActorService			actorService;
	
	/**
	 *We are going to test the requirement 23
	 *
	 *An actor who is authenticated must be able to:
	 *
	 *	1. Manage his or her social profiles, which includes listing, showing, creating, updating,
	 *	and deleting them.
	 *
	 */
	
	@Test
	public void driverCreateSocialProfile() {
		
		Object testingData[][] = {

				{
					//Positive: A company is creating a social profile
					"company1", "exampleName", "company_1", "https://twitter.com", null
				}
					//We can't test negative tests because of the reconstruct, the social profile is validated.
				
					/**Data coverage:
					 * 
					 * 1/5
					 * 
					 */
			
				
			};

			for (int i = 0; i < testingData.length; i++)
				this.templeteCreateSocialProfile((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);

		
	}
	
	@Test
	public void driverDeleteSocialProfile() {
		
		Object testingData[][] = {

				{
					//Positive: An admin is deleting his or her social profile
					"admin1", "socialProfile1", null
				},{
					//Negative: A company is trying to delete the admin1's social profile
					"company1", "socialProfile1", IllegalArgumentException.class
			}
				/**Data coverage:
				 * 
				 * 2/2
				 * 
				 */
		
				
			};

			for (int i = 0; i < testingData.length; i++)
				this.templeteDeleteSocialProfile((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);

		
	}
	
	/**
	 * 
	 * Code Coverage:
	 * 
	 * SocialProfileService: 43.9%. Covered Instruction: 65, Missed Instructions: 83, Total: 148
	 * 
	 */
	
	private void templeteDeleteSocialProfile(String username, String profile, Class<?> expected) {

		this.startTransaction();
		
		super.authenticate(username);
		
		Integer id =this.getEntityId(profile);
		
		Class<?> caught = null;

		try {
				
			SocialProfile socialProfile = this.socialProfileService.findOne(id);
			
			this.socialProfileService.deleteSocialProfile(socialProfile);
			

		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.unauthenticate();
		
		super.checkExceptions(expected, caught);
		
		this.rollbackTransaction();
		
		
	}

	protected void templeteCreateSocialProfile(String username,String nick, String name, String profileLink, Class<?> expected) {
	
		this.startTransaction();
		
		super.authenticate(username);
		
		Class<?> caught = null;

		try {

			
			SocialProfile socialProfile = this.socialProfileService.create(nick, name, profileLink);
			
			socialProfile = this.socialProfileService.save(socialProfile);
			
			Actor logguedActor = this.actorService.loggedActor();
			
			List<SocialProfile> socialProfiles = logguedActor.getSocialProfiles();

			socialProfiles.add(socialProfile);
			logguedActor.setSocialProfiles(socialProfiles);

			this.actorService.save(logguedActor);
			
			

		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.unauthenticate();
		
		super.checkExceptions(expected, caught);
		
		this.rollbackTransaction();
		

	}

}
