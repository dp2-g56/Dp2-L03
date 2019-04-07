package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import domain.PersonalData;
import domain.PositionData;
import repositories.PositionDataRepository;

@Service
@Transactional
public class PositionDataService {
	
	@Autowired
	private PositionDataRepository positionDataRepository;
	
	public void save(PositionData p) {
		this.positionDataRepository.save(p);
	}
	
	public void deleteInBatch(Iterable<PositionData> entities) {
		this.positionDataRepository.deleteInBatch(entities);
	}

}
