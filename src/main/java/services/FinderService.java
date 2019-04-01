
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
			filter = this.finderRepository.getParadesByDeadline(finder.getDeadLine());
			result.retainAll(filter);
		}
		
		// Max deadline
		if (finder.getMaxDeadLine() != null) {
			filter = this.finderRepository.getParadesByMaxDeadline(finder.getMaxDeadLine());
			result.retainAll(filter);
		}
		
		// Min salary
		if (!finder.getMinSalary().equals(null) && !finder.getMinSalary().equals(0.)) {
			filter = this.finderRepository.getParadesByMinSalary(finder.getMinSalary());
			result.retainAll(filter);
		}
		
		finder.setPositions(result);
		Finder finderRes = this.finderRepository.save(finder);
		hacker.setFinder(finderRes);
		this.hackerService.save(hacker);
	}

	public Finder save(Finder finder) {
		return this.finderRepository.save(finder);
	}

	public Finder findOne(Integer id) {
		return this.finderRepository.findOne(id);
	}

	public List<Position> getPositionsByKeyWord(String keyWord) {
		return this.finderRepository.getPositionsByKeyWord("%" + keyWord + "%");
	}	
	
}
