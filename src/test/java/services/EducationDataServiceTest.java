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
import domain.EducationData;
import domain.Finder;
import domain.Mark;
import domain.PersonalData;
import domain.Position;
import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@Transactional
public class EducationDataServiceTest extends AbstractTest {

	@Autowired
	private CurriculumService curriculumService;
	@Autowired
	private HackerService hackerService;
	@Autowired
	private EducationDataService educationDataService;
	
	/**
	 * R17. An actor who is authenticated as a hacker must be able to:
	 *
	 * 1. Manage his or her curricula, which includes deleting them.
	 * 
	 * Ratio of data coverage: 100%
	 *  - Access as a hacker or not.
	 *  - Delete an education data that does belongs to the hacker logged in or not.
	 * 
	 **/
	@Test
	public void driverDeleteEducationData() {

		Object testingData[][] = {

				/**
				 * POSITIVE TEST: Hacker is deleting one of his education data
				 **/
				{ "hacker1", super.getEntityId("educationData1"), null},
				/**
				 * NEGATIVE TEST: Hacker is trying to delete an education data from other hacker
				 **/
				{ "hacker2", super.getEntityId("educationData1"), IllegalArgumentException.class},
				/**
				 * NEGATIVE TEST: Another user is trying to delete an education data
				 **/
				{ "company1", super.getEntityId("educationData1"), IllegalArgumentException.class}
		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteEducationDataTemplate((String) testingData[i][0], (Integer) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	private void deleteEducationDataTemplate(String hacker, int educationDataId, Class<?> expected) {
		
		Class<?> caught = null;

		try {
			super.startTransaction();
			super.authenticate(hacker);
			
			this.educationDataService.deleteEducationDataAsHacker(educationDataId);
			
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
	 * - Create an education data in a curriculum that does belongs to the hacker logged in or not.
	 * - 4 attributes with domain restrictions.
	 * 
	 **/
	@Test
	public void driverCreateEducationData() {
		
		Calendar c1 = Calendar.getInstance();
		c1.set(Calendar.MONTH, 1);
		Date fecha1 = c1.getTime();
		
		c1.set(Calendar.MONTH, 2);
		Date fecha2 = c1.getTime();

		Object testingData[][] = {

				/**
				 * POSITIVE TEST: Hacker is creating an education data
				 **/
				{ "hacker1", super.getEntityId("curriculum1"), "Degree", "Institution", Mark.A, fecha1, fecha2, null},
				/**
				 * NEGATIVE TEST: Another user is trying to create an education data
				 **/
				{ "company", super.getEntityId("curriculum1"), "Degree", "Institution", Mark.A, fecha1, fecha2, IllegalArgumentException.class},
				/**
				 * NEGATIVE TEST: Hacker is trying to create an education data in a curriculum of other hacker
				 **/
				{ "hacker2", super.getEntityId("curriculum1"), "Degree", "Institution", Mark.A, fecha1, fecha2, IllegalArgumentException.class},
				/**
				 * NEGATIVE TEST: Hacker is creating an education data with a degree in blank
				 **/
				{ "hacker1", super.getEntityId("curriculum1"), "", "Institution", Mark.A, fecha1, fecha2, ConstraintViolationException.class},
		};

		for (int i = 0; i < testingData.length; i++)
			this.createEducationDataTemplate((String) testingData[i][0], (Integer) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Mark) testingData[i][4], (Date) testingData[i][5], (Date) testingData[i][6], (Class<?>) testingData[i][7]);
	}

	private void createEducationDataTemplate(String hacker, Integer curriculumId, String degree, String institution, Mark mark, Date startDate, Date endDate, Class<?> expected) {
		
		EducationData educationData = new EducationData();
		educationData.setDegree(degree);
		educationData.setInstitution(institution);
		educationData.setMark(mark);
		educationData.setStartDate(startDate);
		educationData.setEndDate(endDate);
		
		Class<?> caught = null;

		try {
			super.startTransaction();
			super.authenticate(hacker);
			
			this.educationDataService.addOrUpdateEducationDataAsHacker(educationData, curriculumId);
			
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
	 * - Edit an education data that does belongs to the hacker logged in or not.
	 * - 4 attributes with domain restrictions.
	 * 
	 **/
	@Test
	public void driverUpdateEducationData() {
		
		Calendar c1 = Calendar.getInstance();
		c1.set(Calendar.MONTH, 1);
		Date fecha1 = c1.getTime();
		
		c1.set(Calendar.MONTH, 2);
		Date fecha2 = c1.getTime();
	
		Object testingData[][] = {
	
				/**
				 * POSITIVE TEST: Hacker is updating an education data
				 **/
				{ "hacker1", super.getEntityId("educationData1"), super.getEntityId("curriculum1"), "Degree", "Institution", Mark.A, fecha1, fecha2, null},
				/**
				 * NEGATIVE TEST: Another user is trying to update an education data
				 **/
				{ "company", super.getEntityId("educationData1"), super.getEntityId("curriculum1"), "Degree", "Institution", Mark.A, fecha1, fecha2, IllegalArgumentException.class},
				/**
				 * NEGATIVE TEST: Hacker is trying to update an education data of other hacker
				 **/
				{ "hacker2", super.getEntityId("educationData1"), super.getEntityId("curriculum3"), "Degree", "Institution", Mark.A, fecha1, fecha2, IllegalArgumentException.class},
				/**
				 * NEGATIVE TEST: Hacker is updating an education data with a degree in blank
				 **/
				{ "hacker1", super.getEntityId("educationData1"), super.getEntityId("curriculum1"), "", "Institution", Mark.A, fecha1, fecha2, ConstraintViolationException.class},
		};
	
		for (int i = 0; i < testingData.length; i++)
			this.updateEducationDataTemplate((String) testingData[i][0], (Integer) testingData[i][1], (Integer) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Mark) testingData[i][5], (Date) testingData[i][6], (Date) testingData[i][7], (Class<?>) testingData[i][8]);
	}
	
	private void updateEducationDataTemplate(String hacker, Integer educationDataId, Integer curriculumId, String degree, String institution, Mark mark, Date startDate, Date endDate, Class<?> expected) {
		
		EducationData educationData = this.educationDataService.findOne(educationDataId);
		educationData.setDegree(degree);
		educationData.setInstitution(institution);
		educationData.setMark(mark);
		educationData.setStartDate(startDate);
		educationData.setEndDate(endDate);
		
		Class<?> caught = null;
	
		try {
			super.startTransaction();
			super.authenticate(hacker);
			
			this.educationDataService.addOrUpdateEducationDataAsHacker(educationData, curriculumId);
			
			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.rollbackTransaction();
		}
	
		super.checkExceptions(expected, caught);
	
	}

}
