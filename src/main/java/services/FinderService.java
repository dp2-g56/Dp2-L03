
package services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.CompanyRepository;
import repositories.FinderRepository;
import repositories.HackerRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Company;
import domain.Finder;
import domain.Hacker;
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

	public List<Position> finderList(Finder finder) {
		this.hackerService.securityAndHacker();
		
		List<Position> positions = new ArrayList<>();
		List<Position> finderPositions = finder.getPositions();
		
		if(finder.getLastEdit()!=null) {
			// Current Date
			Date currentDate = new Date();

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(currentDate);
			Integer currentDay = calendar.get(Calendar.DATE);
			Integer currentMonth = calendar.get(Calendar.MONTH);
			Integer currentYear = calendar.get(Calendar.YEAR);
			Integer currentHour = calendar.get(Calendar.HOUR);
			
			// LastEdit Finder
			Date lasEdit = finder.getLastEdit();
			calendar.setTime(lasEdit);
			Integer lastEditDay = calendar.get(Calendar.DATE);
			Integer lastEditMonth = calendar.get(Calendar.MONTH);
			Integer lastEditYear = calendar.get(Calendar.YEAR);
			Integer lastEditHour = calendar.get(Calendar.HOUR);	
			
			Integer time = this.configurationService.getConfiguration().getTimeFinder();
			
			if (currentDay.equals(lastEditDay) && currentMonth.equals(lastEditMonth) && currentYear.equals(lastEditYear)
					&& lastEditHour < (currentHour + time)) {
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
		
		List<Position> positions = this.positionService.getFinalPositions();
		
		Date date = new Date();
		
		finder.setDeadLine(null);
		finder.setKeyWord("");
		finder.setLastEdit(date);
		finder.setMaxDeadLine(null);
		finder.setMinSalary(0.);
		finder.setPositions(positions);
		this.finderRepository.save(finder);
		
		return positions;
	}	
	
}
