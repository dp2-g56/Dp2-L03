package services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import domain.Curriculum;
import domain.Hacker;
import domain.PersonalData;
import domain.PositionData;
import repositories.PositionDataRepository;

@Service
@Transactional
public class PositionDataService {
	
	@Autowired
	private PositionDataRepository positionDataRepository;
	@Autowired
	private HackerService hackerService;
	@Autowired
	private CurriculumService curriculumService;
	
	public void save(PositionData p) {
		this.positionDataRepository.save(p);
	}
	
	public void deleteInBatch(Iterable<PositionData> entities) {
		this.positionDataRepository.deleteInBatch(entities);
	}

	public void addOrUpdatePositionDataAsHacker(PositionData positionData, int curriculumId) {
		Hacker hacker = this.hackerService.securityAndHacker();
		
		if(positionData.getId()==0) {
			Curriculum curriculum = this.curriculumService.getCurriculumOfHacker(hacker.getId(), curriculumId);
			List<PositionData> positionsData = curriculum.getPositionData();
			positionsData.add(positionData);
			this.curriculumService.save(curriculum);
		} else {
			
		}
	}

}
