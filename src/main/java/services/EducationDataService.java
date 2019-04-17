package services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import domain.Curriculum;
import domain.EducationData;
import domain.Hacker;
import domain.PersonalData;
import domain.PositionData;
import repositories.EducationDataRepository;

@Service
@Transactional
public class EducationDataService {
	
	@Autowired
	private EducationDataRepository educationDataRepository;
	@Autowired
	private HackerService hackerService;
	@Autowired
	private CurriculumService curriculumService;
	
	public void save(EducationData e) {
		this.educationDataRepository.save(e);
	}
	
	public void deleteInBatch(Iterable<EducationData> entities) {
		this.educationDataRepository.deleteInBatch(entities);
	}
	
	public void addOrUpdateEducationDataAsHacker(EducationData educationData, int curriculumId) {
		Hacker hacker = this.hackerService.securityAndHacker();
		
		if(educationData.getId()==0) {
			Curriculum curriculum = this.curriculumService.getCurriculumOfHacker(hacker.getId(), curriculumId);
			Assert.notNull(curriculum);
			List<EducationData> educationsData = curriculum.getEducationData();
			educationsData.add(educationData);
			this.curriculumService.save(curriculum);
			this.curriculumService.flush();
		} else {
			Assert.notNull(this.educationDataRepository.getEducationDataOfHacker(hacker.getId(), educationData.getId()));
			this.save(educationData);
			this.flush();
		}
	}

	private void flush() {
		this.educationDataRepository.flush();
	}

	public EducationData findOne(int educationDataId) {
		return this.educationDataRepository.findOne(educationDataId);
	}

	public void deleteEducationDataAsHacker(int educationDataId) {
		Hacker hacker = this.hackerService.securityAndHacker();
		Assert.notNull(this.educationDataRepository.getEducationDataOfHacker(hacker.getId(), educationDataId));
		EducationData educationData = this.findOne(educationDataId);
		Curriculum curriculum = this.curriculumService.getCurriculumOfEducationData(educationDataId);
		List<EducationData> educationsData = curriculum.getEducationData();
		educationsData.remove(educationData);
		curriculum.setEducationData(educationsData);
		this.curriculumService.save(curriculum);
		this.educationDataRepository.delete(educationData);
	}

}
