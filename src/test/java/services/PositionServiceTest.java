package services;

import java.util.ArrayList;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import domain.Position;
import domain.Problem;
import forms.FormObjectPositionProblemCheckbox;
import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class PositionServiceTest extends AbstractTest{
	
	@Autowired
	private PositionService		positionService;
	@Autowired
	private ProblemService		problemService;
	
	
	/**
	 * We are going to test the Requirement 9.1:
	 * 
	 * An actor who is authenticated as a company must be able to:
	 * Manage their positions, which includes listing, showing, creating, updating, and deleting them.
	 * Positions can be saved in draft mode; they are not available publicly
	 * until they are saved in final mode. Once a position is saved in final mode, it cannot
	 * be further edited, but it can be cancelled. A position cannot be saved in final mode
	 * unless there are at least two problems associated with it
	 * 
	 * 
	 * 
	 * SENTENCES COVERAGE: 
	 * 
	 * 		Coverage: 73.8%
	 * 		Covered Instructions: 553
	 * 		Missed Instructions: 196
	 * 		Total: 749
	 * 
	 *  **/
	


	//---------------------------------CREATE TEST-------------------------------------//
	
	@SuppressWarnings("unchecked")
	@Test
	public void driverCreatePosition() {
		
		//Deadline
		Date deadline = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(deadline);
		cal.set(2020, 12, 01);
		deadline = cal.getTime();
		
		// Problem List
		
		List<Integer> problems = new ArrayList<>();
		problems.add(this.getEntityId("problem1"));
		problems.add(this.getEntityId("problem2"));
		
		List<Integer> problemsBad = new ArrayList<>();
		
		problemsBad.add(0);
		
		List<Integer> problemsVoid = new ArrayList<>();
		
		//Required Skills
		String requiredSkills = "PHP";
		
		String requiredSkills2 = "Java, MySQL";
		
		//Required Technologies
		String requiredTecnologies = "CodeIgniter";
		
		String requiredTecnologies2 = "Apache, Tomcat";
		
		
		
		Object testingData[][] = {		
				
				/**POSITIVE TEST: A Company is creating a simple position with some problems in draft Mode. **/
				{"company1", "Example", deadline, "description", true, 220.0, "Programmer" ,
						requiredSkills, requiredTecnologies, problems, null},
				
				/**POSITIVE TEST: A Company is creating a position with some problems, in draft mode and
				 * some required skills. **/
				{"company1", "Example", deadline, "description", true, 240.0, "Programmer" ,
							requiredSkills2, requiredTecnologies, problems, null},
				
				/**POSITIVE TEST: A Company is creating a position with some problems, in draft mode and
				 * some required technologies. **/
				{"company1", "Example", deadline, "description", true, 220.0, "Programmer" ,
								requiredSkills, requiredTecnologies2, problems, null},
				
				/**POSITIVE TEST: A Company is creating a position with some problems, in draft mode,
				 * some required technologies and some required skills. **/
				{"company1", "Example", deadline, "description", false, 220.0, "Programmer" ,
								requiredSkills2, requiredTecnologies2, problems, null},
				
				/**NEGATIVE TEST: Company2 is trying to create a Position with the Company1's problems **/
				{"company2", "Example", deadline, "description", false, 220.0, "Programmer" ,
								requiredSkills, requiredTecnologies, problems, IllegalArgumentException.class},
				
				/**NEGATIVE TEST: Company1 is trying to create a Position with a not valid problem id**/
				{"company1", "title", deadline, "description", false, 220.0, "Programmer" ,
								requiredSkills, requiredTecnologies, problemsBad, IllegalArgumentException.class},
				
				/**NEGATIVE TEST: Company1 is trying to create a Position in final mode with no problems **/
				{"company1", "title", deadline, "description", false, 220.0, "Programmer" ,
								requiredSkills, requiredTecnologies, problemsVoid, IllegalArgumentException.class},
				
				
				/** 
				 * DATA COVERAGE:
				 * 		We can't test the not blank and not null restriction, because the form object is already 
				 * 		validate by the controller (With @Valid). So we considered as not tested.
				 * 
				 * 		46.7 %
				 * **/		
				
		};
		
		for (int i = 0; i < testingData.length; i++) {
			this.templateCreatePosition((String) testingData[i][0],(String) testingData[i][1] ,
					(Date) testingData[i][2], (String) testingData[i][3],(Boolean) testingData[i][4],
					(Double) testingData[i][5],(String) testingData[i][6], (String) testingData[i][7],
					(String) testingData[i][8], (List<Integer>) testingData[i][9],(Class<?>) testingData[i][10]);
		}
	}
	
	protected void templateCreatePosition(String company,String title, Date deadline, String description, Boolean isDraftMode, Double offeredSalary, 
			String requiredProfile, String requiredSkills, String requiredTecnologies, List<Integer> problems,  Class<?> expected) {
		
		
		this.startTransaction();
		
		super.authenticate(company);

		Class<?> caught = null;
		


		try {

			FormObjectPositionProblemCheckbox form = new FormObjectPositionProblemCheckbox();
			form.setId(0);
			form.setDeadline(deadline);
			form.setDescription(description);
			form.setIsDraftMode(isDraftMode);
			form.setProblems(problems);
			form.setRequiredSkills(requiredSkills);
			form.setRequiredProfile(requiredProfile);
			form.setTitle(title);
			form.setOfferedSalary(offeredSalary);
			form.setRequiredTecnologies(requiredTecnologies);
			
			List<Problem> problemsList = this.problemService.reconstructList(form);

			Position position = this.positionService.reconstructCheckBox(form, null);

			this.positionService.saveAssignList(position, problemsList);
		
		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
		super.unauthenticate();

		this.rollbackTransaction();

	}
	
	
	//---------------------------------EDIT TEST-------------------------------------//
	
	@SuppressWarnings("unchecked")
	@Test
	public void driverEditPosition() {
		
		
		//Deadline
		Date deadline = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(deadline);
		cal.set(2020, 12, 01);
		deadline = cal.getTime();
		
		// Problem List
		
		List<Integer> problems = new ArrayList<>();
		problems.add(this.getEntityId("problem1"));
		problems.add(this.getEntityId("problem2"));
		
		List<Integer> problemsBad = new ArrayList<>();
		
		problemsBad.add(0);
		
		List<Integer> problemsVoid = new ArrayList<>();
		
		//Required Skills
		String requiredSkills = "PHP";
		
		String requiredSkills2 = "Java, MySQL";
		
		//Required Technologies
		String requiredTecnologies = "CodeIgniter";
		
		String requiredTecnologies2 = "Apache, Tomcat";
		
		
		//Position in DraftMode
		Integer position3Id = this.getEntityId("position3");
		
		//Position in Final Mode
		
		Integer position1Id = this.getEntityId("position1");
		
	Object testingData[][] = {
				
			/**POSITIVE TEST: Company1 is editing a Position in draft mode **/
			{"company1", position3Id ,"Example", deadline, "description", true, 220.0, "Programmer" ,
					requiredSkills, requiredTecnologies, problems, null},
			
			/**POSITIVE TEST: Company1 is editing a Position in draft mode to final mode **/
			{"company1", position3Id ,"Example", deadline, "description", false, 230.0, "Programmer" ,
						requiredSkills2, requiredTecnologies2, problems, null},
			
			/**NEGATIVE TEST: Company2 is trying to edit the Company1's position**/
			{"company2", position3Id ,"Example", deadline, "description", true, 220.0, "Programmer" ,
						requiredSkills, requiredTecnologies, problems, IllegalArgumentException.class},
				
			/**NEGATIVE TEST: Company1 is trying to edit a position that is in final mode**/
			{"company1", position1Id ,"Example", deadline, "description", true, 220.0, "Programmer" ,
							requiredSkills, requiredTecnologies, problems, IllegalArgumentException.class},
			
			/**NEGATIVE TEST: Company1 is trying to publish a position with no problems.**/
			{"company1", position3Id ,"Example", deadline, "description", false, 220.0, "Programmer" ,
								requiredSkills, requiredTecnologies, problemsVoid, IllegalArgumentException.class},
		
			
			/** 
			 * DATA COVERAGE:
			 * 		We can't test the not blank and not null restriction, because the form object is already 
			 * 		validate by the controller (With @Valid). So we considered as not tested.
			 * 
			 * 		46.1%
			 * **/		
			
			
		};
	
	for (int i = 0; i < testingData.length; i++) {
		this.templateEditPosition((String) testingData[i][0],(Integer) testingData[i][1], (String) testingData[i][2] ,
				(Date) testingData[i][3], (String) testingData[i][4],(Boolean) testingData[i][5],
				(Double) testingData[i][6],(String) testingData[i][7], (String) testingData[i][8],
				(String) testingData[i][9], (List<Integer>) testingData[i][10],(Class<?>) testingData[i][11]);
		
		}
	}
	
	protected void templateEditPosition(String username, Integer positionId ,String title, Date deadline, String description, Boolean isDraftMode, Double offeredSalary, 
			String requiredProfile, String requiredSkills, String requiredTecnologies, List<Integer> problems,  Class<?> expected){
		
		this.startTransaction();
		super.authenticate(username);
		
		

		Class<?> caught = null;
		try {
			
			FormObjectPositionProblemCheckbox form = this.positionService.prepareFormObjectPositionProblemCheckbox(positionId);

			
			form.setDeadline(deadline);
			form.setDescription(description);
			form.setIsDraftMode(isDraftMode);
			form.setProblems(problems);
			form.setRequiredSkills(requiredSkills);
			form.setRequiredProfile(requiredProfile);
			form.setTitle(title);
			form.setOfferedSalary(offeredSalary);
			form.setRequiredTecnologies(requiredTecnologies);

			List<Problem> problemsList = this.problemService.reconstructList(form);

			Position position = this.positionService.reconstructCheckBox(form, null);
			this.positionService.saveAssignList(position, problemsList);
			
			this.positionService.flush();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
		super.unauthenticate();

		this.rollbackTransaction();
	}
	
	//---------------------------------DELETE TEST-------------------------------------//
	
	
	@Test	
	public void driverDeletePosition() {
		
		//Position in draft Mode
		Integer position3Id = this.getEntityId("position3");
		
		//Position in final Mode
		Integer position1Id = this.getEntityId("position1");
		
		Object testingData[][] = {
				
				/**POSITIVE TEST: Company1 is deleting his or her position **/
				{"company1", position3Id, null},
				
				/**NEGATIVE TEST: Company1 is trying to delete a position in final mode**/
				{"company1", position1Id,  IllegalArgumentException.class},
				
				/**NEGATIVE TEST: Company2 is trying to delete the company1's position**/
				{"company2", position3Id, IllegalArgumentException.class},
				
				/** 
				 * DATA COVERAGE:
				 * 
				 *  	75%
				 * **/
				
		
		};
		
		for (int i = 0; i < testingData.length; i++) {
			this.templeteDeletePosition((String)testingData[i][0], (Integer) testingData[i][1],(Class<?>) testingData[i][2]);
			
		}
		
		
	}
	
	protected void templeteDeletePosition(String username, Integer positionId, Class<?> expected) {
		
		this.startTransaction();

		super.authenticate(username);
		Class<?> caught = null;
		try {
			this.positionService.deletePositionWithId(positionId);
			this.positionService.flush();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
		super.unauthenticate();

		this.rollbackTransaction();
	}

}
