
package services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.FinderRepository;
import domain.Finder;
import domain.Position;

@Service
@Transactional
public class FinderService {

	@Autowired
	private FinderRepository		finderRepository;

	@Autowired
	private ConfigurationService	configurationService;


	public List<Finder> findAll() {
		return this.finderRepository.findAll();
	}

	public Finder findOne(int id) {
		return this.finderRepository.findOne(id);
	}

	public Finder save(Finder finder) {
		return this.finderRepository.save(finder);
	}

	public void delete(Finder finder) {
		this.finderRepository.delete(finder);
	}

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

		// Empty List parades
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
}
