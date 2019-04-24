
package services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import repositories.CompanyRepository;
import repositories.FinderRepository;
import repositories.HackerRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Company;
import domain.Finder;
import domain.Hacker;
import domain.Message;
import domain.Position;
import domain.Problem;

@Service
@Transactional
public class FinderService {

	@Autowired
	private FinderRepository finderRepository;
	@Autowired
	private ConfigurationService configurationService;
	@Autowired
	private HackerService hackerService;
	@Autowired
	private PositionService positionService;
	@Autowired
	private	MessageService	messageService;
	@Autowired
	private Validator validator;

	public List<Position> finderList(Finder finder) {
		Hacker hacker = this.hackerService.securityAndHacker();
		
		Assert.notNull(finder);
		Assert.isTrue(finder.getId()>0);
		Assert.isTrue(hacker.getFinder().getId() == finder.getId());
		
		List<Position> positions = new ArrayList<>();
		List<Position> finderPositions = finder.getPositions();
		
		if(finder.getLastEdit()!=null) {
			// Current Date
			Date currentDate = new Date();

			Calendar calendar1 = Calendar.getInstance();
			calendar1.setTime(currentDate);
			
			// LastEdit Finder
			Date lastEdit = finder.getLastEdit();
			
			Calendar calendar2 = Calendar.getInstance();
			calendar2.setTime(lastEdit);
			
			Integer time = this.configurationService.getConfiguration().getTimeFinder();
			
			calendar2.add(Calendar.HOUR, time);
			
			if (calendar2.after(calendar1)) {
				Integer numFinderResult = this.configurationService.getConfiguration().getFinderResult();

				if (finderPositions.size() > numFinderResult)
					for (int i = 0; i < numFinderResult; i++)
						positions.add(finderPositions.get(i));
				else
					positions = finderPositions;
			}
		}
		
		return positions;
	}

	public List<Position> getFinalPositionsAndCleanFinder(Finder finder) {
		Hacker hacker = this.hackerService.securityAndHacker();
		
		Assert.notNull(finder);
		Assert.isTrue(finder.getId()>0);
		Assert.isTrue(hacker.getFinder().getId() == finder.getId());
		
		List<Position> positions = this.positionService.getFinalPositions();
		
		Date date = new Date();
		
		finder.setDeadLine(null);
		finder.setKeyWord("");
		finder.setLastEdit(date);
		finder.setMaxDeadLine(null);
		finder.setMinSalary(0.);
		finder.setPositions(positions);
		this.finderRepository.save(finder);
		
		this.finderRepository.flush();
		
		return positions;
	}

	public Finder reconstruct(Finder finderForm, BindingResult binding) {
		Finder result = new Finder();
		
		Finder finder = this.finderRepository.findOne(finderForm.getId());
		
		result.setId(finder.getId());
		result.setVersion(finder.getVersion());
		result.setPositions(finder.getPositions());
		
		Date date = new Date();
		result.setLastEdit(date);
		
		result.setKeyWord(finderForm.getKeyWord());
		result.setDeadLine(finderForm.getDeadLine());
		result.setMaxDeadLine(finderForm.getMaxDeadLine());
		
		result.setMinSalary(finderForm.getMinSalary());
		
		this.validator.validate(result, binding);
		
		return result;
	}

	public void filterPositionsByFinder(Finder finder) {
		Hacker hacker = this.hackerService.securityAndHacker();
		
		Assert.notNull(finder);
		Assert.isTrue(finder.getId()>0);
		Assert.isTrue(hacker.getFinder().getId() == finder.getId());
		
		List<Position> filter = new ArrayList<>();
		List<Position> result = this.positionService.getFinalPositions();
		
		// Keyword
		if(!finder.getKeyWord().equals(null) && !finder.getKeyWord().contentEquals("")) {
			filter = this.getPositionsByKeyWord(finder.getKeyWord());
			result.retainAll(filter);
		}
		
		// Deadline
		if (finder.getDeadLine() != null) {
			filter = this.finderRepository.getPositionsByDeadline(finder.getDeadLine());
			result.retainAll(filter);
		}
		
		// Max deadline
		if (finder.getMaxDeadLine() != null) {
			filter = this.finderRepository.getPositionsByMaxDeadline(finder.getMaxDeadLine());
			result.retainAll(filter);
		}
		
		// Min salary
		try {
			Assert.notNull(finder.getMinSalary());
			filter = this.finderRepository.getPositionsByMinSalary(finder.getMinSalary());
			result.retainAll(filter);
		} catch(Throwable oops) {
			finder.setMinSalary(0.);
		}
		
		finder.setPositions(result);
		
		Finder finderRes = this.finderRepository.save(finder);
		hacker.setFinder(finderRes);
		this.hackerService.save(hacker);
	}

	public List<Finder> findAll() {
		return this.finderRepository.findAll();
	}

	public Finder findOne(int id) {
		return this.finderRepository.findOne(id);
	}

	public Finder save(Finder finder) {
		return this.finderRepository.save(finder);
	}

	public List<Position> getPositionsByKeyWord(String keyWord) {
		return this.finderRepository.getPositionsByKeyWord("%" + keyWord + "%");
	}

	public void delete(Finder finder) {
		this.finderRepository.delete(finder);
	}

	/*
	 * public void updateFinders() {
	 * 
	 * Date thisMoment = new Date();
	 * 
	 * Calendar calendar = Calendar.getInstance();
	 * calendar.setTime(thisMoment);
	 * calendar.add(Calendar.HOUR_OF_DAY, -this.configurationService.getConfiguration().getTimeFinder());
	 * 
	 * List<Finder> finders = new ArrayList<Finder>();
	 * finders = this.finderRepository.getFindersNeedToUpdate(calendar.getTime());
	 * for (Finder f : finders)
	 * f.setPositions(new ArrayList<Position>());
	 */

	//}

	public void updateAllFinders() {

		// LastEdit Finder

		List<Finder> finders = this.findAll();
		// Current Date
		Date currentDate = new Date();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentDate);
		Integer currentDay = calendar.get(Calendar.DATE);
		Integer currentMonth = calendar.get(Calendar.MONTH);
		Integer currentYear = calendar.get(Calendar.YEAR);
		Integer currentHour = calendar.get(Calendar.HOUR);

		// Max Time Finder
		Integer time = this.configurationService.getConfiguration().getTimeFinder();

		// Empty List Positions
		List<Position> positions = new ArrayList<>();

		for (Finder f : finders) {

			// Last Edit Date
			Date lastEdit = f.getLastEdit();
			calendar.setTime(lastEdit);
			Integer lastEditDay = calendar.get(Calendar.DATE);
			Integer lastEditMonth = calendar.get(Calendar.MONTH);
			Integer lastEditYear = calendar.get(Calendar.YEAR);
			Integer lastEditHour = calendar.get(Calendar.HOUR);
			if (!(currentDay.equals(lastEditDay) && currentMonth.equals(lastEditMonth) && currentYear.equals(lastEditYear) && lastEditHour < (currentHour + time))) {
				f.setPositions(positions);
				this.save(f);
			}
		}
	}

	public List<Position> getPositionsByMinSalary(Double minSalary) {
		return this.finderRepository.getPositionsByMinSalary(minSalary);
	}

	public void flush() {
		this.finderRepository.flush();
	}

	public void cleanFindersOfPositions(List<Position> positions) {
		for (Position p : positions) {
			List<Finder> finders = this.finderRepository.getFindersContainsPosition(p);
			for (Finder f : finders)
				f.getPositions().removeAll(positions);
		}
	}

	public void sendNotificationPosition(Position position) {

		String rs = "%" + position.getRequiredSkills().get(0) + "%";
		String rt = "%" + position.getRequiredTecnologies().get(0) + "%";
		String title = "%" + position.getTitle() + "%";
		String desription = "%" + position.getDescription() + "%";
		String requiredProfile = "%" + position.getRequiredProfile() + "%";
		String ticker = "%" + position.getTicker() + "%";

		List<Hacker> hackers = this.finderRepository.getHackersThatFinderKeyWordIsContaine(rs, rt, title, desription, requiredProfile, ticker);

		Message message2 = this.messageService.create("Nueva oferta / New offer", "Una nueva oferta concuerda con tu busqueda / A new offer matches your finder criteria", "STATUS, NOTIFICATION", "NOTIFICATION", "");

		for (Hacker a : hackers) {

			message2.setReceiver(a.getUserAccount().getUsername());
			a.getMessages().add(message2);
			this.hackerService.save(a);

		}

	}

}
