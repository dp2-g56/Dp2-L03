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
import org.springframework.util.Assert;

import domain.Finder;
import domain.Position;
import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@Transactional
public class FinderServiceTest extends AbstractTest {

	@Autowired
	private FinderService finderService;
	@Autowired
	private HackerService hackerService;
	@Autowired
	private PositionService positionService;
	@Autowired
	private ConfigurationService configurationService;
	
	/**
	 * R17. An actor who is authenticated as a hacker must be able to:
	 *
	 * 2. Manage his or her finder, which involves clearing it.
	 * 
	 **/
	@Test
	public void driverCleanFinder() {
		Finder finder = this.hackerService.getHackerByUsername("hacker1").getFinder();

		Object testingData[][] = {

				/**
				 * POSITIVE TEST: Hacker is cleaning his finder
				 **/
				{ "hacker1", finder, null },
				/**
				 * NEGATIVE TEST: Hacker is trying to clean another hacker finder
				 **/
				{ "hacker2", finder, IllegalArgumentException.class}
		};

		for (int i = 0; i < testingData.length; i++)
			this.finderCleanTemplate((String) testingData[i][0], (Finder) testingData[i][1],
					(Class<?>) testingData[i][2]);

	}

	private void finderCleanTemplate(String hacker, Finder finder, Class<?> expected) {
		finder.setPositions(new ArrayList<Position> ());
		this.finderService.save(finder);
		
		Class<?> caught = null;

		try {
			super.startTransaction();
			super.authenticate(hacker);
			
			this.finderService.getFinalPositionsAndCleanFinder(finder);
			
			Assert.isTrue(this.finderService.findOne(finder.getId()).getPositions().size() == this.positionService.getFinalPositions().size());
			
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
	 * 2. Manage his or her finder, which involves listing its contents.
	 * 
	 **/
	@Test
	public void driverListFinder() {
		Finder finder = this.hackerService.getHackerByUsername("hacker1").getFinder();

		Object testingData[][] = {

				/**
				 * POSITIVE TEST: Hacker is listing his finder
				 **/
				{ "hacker1", finder, null },
				/**
				 * NEGATIVE TEST: Hacker is trying to list another finder
				 **/
				{ "hacker2", finder, IllegalArgumentException.class }
		};

		for (int i = 0; i < testingData.length; i++)
			this.finderListTemplate((String) testingData[i][0], (Finder) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	private void finderListTemplate(String hacker, Finder finder, Class<?> expected) {
		Class<?> caught = null;

		try {
			super.startTransaction();
			super.authenticate(hacker);
			
			this.finderService.finderList(finder);
			
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
	 * 2. Manage his or her finder, which involves updating the search criteria.
	 * 
	 **/
	@Test
	public void driverUpdateFinder() {
		Finder finder = this.hackerService.getHackerByUsername("hacker1").getFinder();

		Object testingData[][] = {

				/**
				 * POSITIVE TEST: Hacker is updating his finder
				 **/
				{ "hacker1", finder, "Skill", null },
				/**
				 * NEGATIVE TEST: Hacker is trying to update another finder
				 **/
				{ "hacker2", finder, "Skill", IllegalArgumentException.class }
		};

		for (int i = 0; i < testingData.length; i++)
			this.finderUpdateTemplate((String) testingData[i][0], (Finder) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);

	}

	private void finderUpdateTemplate(String hacker, Finder finder, String keyWord, Class<?> expected) {
		finder.setKeyWord(keyWord);
		
		Class<?> caught = null;

		try {
			super.startTransaction();
			super.authenticate(hacker);
			
			this.finderService.filterPositionsByFinder(finder);
			
			List<Position> positions = this.finderService.findOne(finder.getId()).getPositions();
			
			Assert.isTrue(positions.size() == this.finderService.getPositionsByKeyWord(this.finderService.findOne(finder.getId()).getKeyWord()).size());
			
			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.rollbackTransaction();
		}

		super.checkExceptions(expected, caught);

	}

}