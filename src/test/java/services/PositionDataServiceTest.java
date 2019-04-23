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
import domain.PositionData;
import domain.Finder;
import domain.Mark;
import domain.PersonalData;
import domain.Position;
import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@Transactional
public class PositionDataServiceTest extends AbstractTest {

	@Autowired
	private CurriculumService curriculumService;
	@Autowired
	private HackerService hackerService;
	@Autowired
	private PositionDataService positionDataService;
	
	/**
	 * R17. An actor who is authenticated as a hacker must be able to:
	 *
	 * 1. Manage his or her curricula, which includes deleting them.
	 * 
	 * Ratio of data coverage: 100%
	 *  - Access as a hacker or not.
	 *  - Delete a postion data that does belong to the hacker logged in or not.
	 * 
	 **/
	@Test
	public void driverDeletePositionData() {

		Object testingData[][] = {

				/**
				 * POSITIVE TEST: Hacker is deleting one of his position data
				 **/
				{ "hacker1", super.getEntityId("positionData1"), null},
				/**
				 * NEGATIVE TEST: Hacker is trying to delete a position data from other hacker
				 **/
				{ "hacker2", super.getEntityId("positionData1"), IllegalArgumentException.class},
				/**
				 * NEGATIVE TEST: Another user is trying to delete a position data
				 **/
				{ "company1", super.getEntityId("positionData1"), IllegalArgumentException.class}
		};

		for (int i = 0; i < testingData.length; i++)
			this.deletePositionDataTemplate((String) testingData[i][0], (Integer) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	private void deletePositionDataTemplate(String hacker, int positionDataId, Class<?> expected) {
		
		Class<?> caught = null;

		try {
			super.startTransaction();
			super.authenticate(hacker);
			
			this.positionDataService.deletePositionDataAsHacker(positionDataId);
			
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
	 * Ratio of data coverage: 4/7 = 57.14%
	 * - Access as a hacker or not.
	 * - Create a position data in a curriculum that does belongs to the hacker logged in or not.
	 * - 4 attributes with domain restrictions.
	 * 
	 **/
	@Test
	public void driverCreatePositionData() {
		
		Calendar c1 = Calendar.getInstance();
		c1.set(Calendar.MONTH, 1);
		Date fecha1 = c1.getTime();
		
		c1.set(Calendar.MONTH, 2);
		Date fecha2 = c1.getTime();

		Object testingData[][] = {

				/**
				 * POSITIVE TEST: Hacker is creating a position data
				 **/
				{ "hacker1", super.getEntityId("curriculum1"), "Title", "Description", fecha1, fecha2, null},
				/**
				 * NEGATIVE TEST: Another user is trying to create a position data
				 **/
				{ "company", super.getEntityId("curriculum1"), "Title", "Description", fecha1, fecha2, IllegalArgumentException.class},
				/**
				 * NEGATIVE TEST: Hacker is trying to create a position data in a curriculum of other hacker
				 **/
				{ "hacker2", super.getEntityId("curriculum1"), "Title", "Description", fecha1, fecha2, IllegalArgumentException.class},
				/**
				 * NEGATIVE TEST: Hacker is creating a position data with a title in blank
				 **/
				{ "hacker1", super.getEntityId("curriculum1"), "", "Description", fecha1, fecha2, ConstraintViolationException.class}
		};

		for (int i = 0; i < testingData.length; i++)
			this.createPositionDataTemplate((String) testingData[i][0], (Integer) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Date) testingData[i][4], (Date) testingData[i][5], (Class<?>) testingData[i][6]);
	}

	private void createPositionDataTemplate(String hacker, Integer curriculumId, String title, String description, Date startDate, Date endDate, Class<?> expected) {
		
		PositionData positionData = new PositionData();
		positionData.setTitle(title);
		positionData.setDescription(description);
		positionData.setStartDate(startDate);
		positionData.setEndDate(endDate);
		
		Class<?> caught = null;

		try {
			super.startTransaction();
			super.authenticate(hacker);
			
			this.positionDataService.addOrUpdatePositionDataAsHacker(positionData, curriculumId);
			
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
	 * Ratio of data coverage: 4/7 = 57.14%
	 * - Access as a hacker or not.
	 * - Edit a position data that does belongs to the hacker logged in or not.
	 * - 4 attributes with domain restrictions.
	 * 
	 **/
	@Test
	public void driverUpdatePositionData() {
		
		Calendar c1 = Calendar.getInstance();
		c1.set(Calendar.MONTH, 1);
		Date fecha1 = c1.getTime();
		
		c1.set(Calendar.MONTH, 2);
		Date fecha2 = c1.getTime();
	
		Object testingData[][] = {
	
				/**
				 * POSITIVE TEST: Hacker is updating a position data
				 **/
				{ "hacker1", super.getEntityId("positionData1"), super.getEntityId("curriculum1"), "Title", "Description", fecha1, fecha2, null},
				/**
				 * NEGATIVE TEST: Another user is trying to update a position data
				 **/
				{ "company", super.getEntityId("positionData1"), super.getEntityId("curriculum1"), "Title", "Description", fecha1, fecha2, IllegalArgumentException.class},
				/**
				 * NEGATIVE TEST: Hacker is trying to update a position data of other hacker
				 **/
				{ "hacker2", super.getEntityId("positionData1"), super.getEntityId("curriculum3"), "Title", "Description", fecha1, fecha2, IllegalArgumentException.class},
				/**
				 * NEGATIVE TEST: Hacker is updating a position data with a title in blank
				 **/
				{ "hacker1", super.getEntityId("positionData1"), super.getEntityId("curriculum1"), "", "Description", fecha1, fecha2, ConstraintViolationException.class},
		};
	
		for (int i = 0; i < testingData.length; i++)
			this.updatePositionDataTemplate((String) testingData[i][0], (Integer) testingData[i][1], (Integer) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Date) testingData[i][5], (Date) testingData[i][6], (Class<?>) testingData[i][7]);
	}
	
	private void updatePositionDataTemplate(String hacker, Integer positionDataId, Integer curriculumId, String title, String description, Date startDate, Date endDate, Class<?> expected) {
		
		PositionData positionData = this.positionDataService.findOne(positionDataId);
		positionData.setTitle(title);
		positionData.setDescription(description);
		positionData.setStartDate(startDate);
		positionData.setEndDate(endDate);
		
		Class<?> caught = null;
	
		try {
			super.startTransaction();
			super.authenticate(hacker);
			
			this.positionDataService.addOrUpdatePositionDataAsHacker(positionData, curriculumId);
			
			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.rollbackTransaction();
		}
	
		super.checkExceptions(expected, caught);
	
	}

}
