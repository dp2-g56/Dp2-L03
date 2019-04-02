package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import domain.PersonalData;
import repositories.PersonalDataRepository;

@Service
@Transactional
public class PersonalDataService {
	
	@Autowired
	private PersonalDataRepository personalDataRepository;
	
	public void save(PersonalData p) {
		this.personalDataRepository.save(p);
	}

}
