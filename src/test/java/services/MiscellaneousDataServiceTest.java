package services;

import java.util.ArrayList;
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

import domain.Curriculum;
import domain.MiscellaneousData;
import domain.Finder;
import domain.Mark;
import domain.PersonalData;
import domain.Position;
import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@Transactional
public class MiscellaneousDataServiceTest extends AbstractTest {

	@Autowired
	private CurriculumService curriculumService;
	@Autowired
	private HackerService hackerService;
	@Autowired
	private MiscellaneousDataService miscellaneousDataService;
	
	/**
	 * R17. An actor who is authenticated as a hacker must be able to:
	 *
	 * 1. Manage his or her curricula, which includes deleting them.
	 * 
	 * Ratio of data coverage: 100%
	 *  - Access as a hacker or not.
	 *  - Delete a miscellaneous data that does belongs to the hacker logged in or not.
	 * 
	 **/
	@Test
	public void driverDeleteMiscellaneousData() {

		Object testingData[][] = {

				/**
				 * POSITIVE TEST: Hacker is deleting one of his miscellaneous data
				 **/
				{ "hacker1", super.getEntityId("miscellaneousData1"), null},
				/**
				 * NEGATIVE TEST: Hacker is trying to delete an miscellaneous data from other hacker
				 **/
				{ "hacker2", super.getEntityId("miscellaneousData1"), IllegalArgumentException.class},
				/**
				 * NEGATIVE TEST: Another user is trying to delete an miscellaneous data
				 **/
				{ "company1", super.getEntityId("miscellaneousData1"), IllegalArgumentException.class}
		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteMiscellaneousDataTemplate((String) testingData[i][0], (Integer) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	private void deleteMiscellaneousDataTemplate(String hacker, int miscellaneousDataId, Class<?> expected) {
		
		Class<?> caught = null;

		try {
			super.startTransaction();
			super.authenticate(hacker);
			
			this.miscellaneousDataService.deleteMiscellaneousDataAsHacker(miscellaneousDataId);
			
			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.rollbackTransaction();
		}

		super.checkExceptions(expected, caught);

	}
	
	/**
	 * R17. An actor who is authenticated as a hacker must be able to:
	 *
	 * 1. Manage his or her curricula, which includes creating them.
	 * 
	 * Ratio of data coverage: 4/4 = 100%
	 * - Access as a hacker or not.
	 * - Create a miscellaneous data in a curriculum that does belongs to the hacker logged in or not
	 * - 1 attribute with domain restrictions.
	 * 
	 **/
	@Test
	public void driverCreateMiscellaneousData() {
		
		Object testingData[][] = {

				/**
				 * POSITIVE TEST: Hacker is creating a miscellaneous data
				 **/
				{ "hacker1", super.getEntityId("curriculum1"), "FreeText", null},
				/**
				 * NEGATIVE TEST: Another user is trying to create a miscellaneous data
				 **/
				{ "company", super.getEntityId("curriculum1"), "FreeText", IllegalArgumentException.class},
				/**
				 * NEGATIVE TEST: Hacker is trying to create a miscellaneous data in a curriculum of other hacker
				 **/
				{ "hacker2", super.getEntityId("curriculum1"), "FreeText", IllegalArgumentException.class},
				/**
				 * NEGATIVE TEST: Hacker is creating a miscellaneous data with a degree in blank
				 **/
				{ "hacker1", super.getEntityId("curriculum1"), "", ConstraintViolationException.class},
		};

		for (int i = 0; i < testingData.length; i++)
			this.createMiscellaneousDataTemplate((String) testingData[i][0], (Integer) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	private void createMiscellaneousDataTemplate(String hacker, Integer curriculumId, String freeText, Class<?> expected) {
		
		MiscellaneousData miscellaneousData = new MiscellaneousData();
		miscellaneousData.setFreeText(freeText);
		
		Class<?> caught = null;

		try {
			super.startTransaction();
			super.authenticate(hacker);
			
			this.miscellaneousDataService.addOrUpdateMiscellaneousDataAsHacker(miscellaneousData, curriculumId);
			
			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.rollbackTransaction();
		}

		super.checkExceptions(expected, caught);

	}

	/**
	 * R17. An actor who is authenticated as a hacker must be able to:
	 *
	 * 1. Manage his or her curricula, which includes updating them.
	 * 
	 * Ratio of data coverage: 4/4 = 100%
	 * - Access as a hacker or not.
	 * - Edit a miscellaneous data that does belongs to the hacker logged in or not.
	 * - 1 attribute with domain restrictions.
	 * 
	 **/
	@Test
	public void driverUpdateMiscellaneousData() {
	
		Object testingData[][] = {
	
				/**
				 * POSITIVE TEST: Hacker is updating a miscellaneous data
				 **/
				{ "hacker1", super.getEntityId("miscellaneousData1"), super.getEntityId("curriculum1"), "FreeText", null},
				/**
				 * NEGATIVE TEST: Another user is trying to update a miscellaneous data
				 **/
				{ "company", super.getEntityId("miscellaneousData1"), super.getEntityId("curriculum1"), "FreeText", IllegalArgumentException.class},
				/**
				 * NEGATIVE TEST: Hacker is trying to update a miscellaneous data of other hacker
				 **/
				{ "hacker2", super.getEntityId("miscellaneousData1"), super.getEntityId("curriculum3"), "FreeText", IllegalArgumentException.class},
				/**
				 * NEGATIVE TEST: Hacker is updating an miscellaneous data with the free text in blank
				 **/
				{ "hacker1", super.getEntityId("miscellaneousData1"), super.getEntityId("curriculum1"), "", ConstraintViolationException.class},
		};
	
		for (int i = 0; i < testingData.length; i++)
			this.updateMiscellaneousDataTemplate((String) testingData[i][0], (Integer) testingData[i][1], (Integer) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
	}
	
	private void updateMiscellaneousDataTemplate(String hacker, Integer miscellaneousDataId, Integer curriculumId, String freeText, Class<?> expected) {
		
		MiscellaneousData miscellaneousData = this.miscellaneousDataService.findOne(miscellaneousDataId);
		miscellaneousData.setFreeText(freeText);
		
		Class<?> caught = null;
	
		try {
			super.startTransaction();
			super.authenticate(hacker);
			
			this.miscellaneousDataService.addOrUpdateMiscellaneousDataAsHacker(miscellaneousData, curriculumId);
			
			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.rollbackTransaction();
		}
	
		super.checkExceptions(expected, caught);
	
	}
	
	/**
	 * R17. An actor who is authenticated as a hacker must be able to:
	 *
	 * 1. Manage his or her curricula, which includes showing them.
	 * 
	 * Ratio of data coverage: 100%
	 *  - Access as a hacker or not.
	 *  - Show the attachments of a miscellaneous data that does belongs to the hacker logged in or not.
	 * 
	 **/
	@Test
	public void driverListAttachmentsMiscellaneousData() {

		Object testingData[][] = {

				/**
				 * POSITIVE TEST: Hacker is listing the attachments one of his miscellaneous data
				 **/
				{ "hacker1", super.getEntityId("miscellaneousData1"), null},
				/**
				 * NEGATIVE TEST: Hacker is trying to list the attachments of a miscellaneous data from other hacker
				 **/
				{ "hacker2", super.getEntityId("miscellaneousData1"), IllegalArgumentException.class},
				/**
				 * NEGATIVE TEST: Another user is trying to list the attachments of a miscellaneous data
				 **/
				{ "company1", super.getEntityId("miscellaneousData1"), IllegalArgumentException.class}
		};

		for (int i = 0; i < testingData.length; i++)
			this.listAttachmentsMiscellaneousDataTemplate((String) testingData[i][0], (Integer) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	private void listAttachmentsMiscellaneousDataTemplate(String hacker, int miscellaneousDataId, Class<?> expected) {
		
		Class<?> caught = null;

		try {
			super.startTransaction();
			super.authenticate(hacker);
			
			this.miscellaneousDataService.getAttachmentsOfMiscellaneousDataOfLoggedHacker(miscellaneousDataId);
			
			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.rollbackTransaction();
		}

		super.checkExceptions(expected, caught);

	}
	
	/**
	 * R17. An actor who is authenticated as a hacker must be able to:
	 *
	 * 1. Manage his or her curricula, which includes creating and updating them.
	 * 
	 * Ratio of data coverage: 100%
	 * - Access as a hacker or not.
	 * - Create an attachment in a miscellaneous data that does belongs to the hacker logged in or not.
	 * - 1 attribute with domain restrictions.
	 * 
	 **/
	@Test
	public void driverCreateAttachmentsMiscellaneousData() {

		Object testingData[][] = {

				/**
				 * POSITIVE TEST: Hacker is creating an attachment to one of his miscellaneous data
				 **/
				{ "hacker1", super.getEntityId("miscellaneousData1"), "Attachment", null},
				/**
				 * NEGATIVE TEST: Hacker is trying to create an attachment to a miscellaneous data from other hacker
				 **/
				{ "hacker2", super.getEntityId("miscellaneousData1"), "Attachment", IllegalArgumentException.class},
				/**
				 * NEGATIVE TEST: Another user is trying to create an attachment
				 **/
				{ "company1", super.getEntityId("miscellaneousData1"), "Attachment", IllegalArgumentException.class},
				/**
				 * NEGATIVE TEST: Hacker is trying to create an attachment in blank
				 **/
				{ "hacker1", super.getEntityId("miscellaneousData1"), "", IllegalArgumentException.class}
		};

		for (int i = 0; i < testingData.length; i++)
			this.createAttachmentsMiscellaneousDataTemplate((String) testingData[i][0], (Integer) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);

	}

	private void createAttachmentsMiscellaneousDataTemplate(String hacker, int miscellaneousDataId, String attachment, Class<?> expected) {
		
		Class<?> caught = null;

		try {
			super.startTransaction();
			super.authenticate(hacker);
			
			this.miscellaneousDataService.addAttachmentAsHacker(miscellaneousDataId, attachment);
			
			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.rollbackTransaction();
		}

		super.checkExceptions(expected, caught);

	}
	
	/**
	 * R17. An actor who is authenticated as a hacker must be able to:
	 *
	 * 1. Manage his or her curricula, which includes updating them.
	 * 
	 * Ratio of data coverage: 100%
	 * - Access as a hacker or not.
	 * - Delete an attachment in a miscellaneous data that does belongs to the hacker logged in or not.
	 * - Delete an attachment that exists or not.
	 * 
	 **/
	@Test
	public void driverDeleteAttachmentMiscellaneousData() {

		Object testingData[][] = {

				/**
				 * POSITIVE TEST: Hacker is deleting an attachment of one of his miscellaneous data
				 **/
				{ "hacker1", super.getEntityId("miscellaneousData1"), 0, null},
				/**
				 * NEGATIVE TEST: Hacker is trying to delete an attachment of a miscellaneous data from other hacker
				 **/
				{ "hacker2", super.getEntityId("miscellaneousData1"), 0, IllegalArgumentException.class},
				/**
				 * NEGATIVE TEST: Another user is trying to delete an attachment of a miscellaneous data
				 **/
				{ "company1", super.getEntityId("miscellaneousData1"), 0, IllegalArgumentException.class},
				/**
				 * NEGATIVE TEST: Hacker is trying to delete an attachment that doesn't exist
				 **/
				{ "hacker1", super.getEntityId("miscellaneousData1"), 3, IllegalArgumentException.class},
		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteAttachmentMiscellaneousDataTemplate((String) testingData[i][0], (Integer) testingData[i][1], (Integer) testingData[i][2], (Class<?>) testingData[i][3]);

	}

	private void deleteAttachmentMiscellaneousDataTemplate(String hacker, int miscellaneousDataId, int attachmentIndex, Class<?> expected) {
		
		Class<?> caught = null;

		try {
			super.startTransaction();
			super.authenticate(hacker);
			
			this.miscellaneousDataService.deleteAttachmentAsHacker(miscellaneousDataId, attachmentIndex);
			
			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.rollbackTransaction();
		}

		super.checkExceptions(expected, caught);

	}

}
