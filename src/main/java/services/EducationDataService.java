package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import domain.EducationData;
import repositories.EducationDataRepository;

@Service
@Transactional
public class EducationDataService {
	
	@Autowired
	private EducationDataRepository educationDataRepository;
	
	public void save(EducationData e) {
		this.educationDataRepository.save(e);
	}

}