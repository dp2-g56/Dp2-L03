package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import domain.MiscellaneousData;
import repositories.MiscellaneousDataRepository;

@Service
@Transactional
public class MiscellaneousDataService {
	
	@Autowired
	private MiscellaneousDataRepository miscellaneousDataRepository;
	
	public void save(MiscellaneousData m) {
		this.miscellaneousDataRepository.save(m);
	}

}
