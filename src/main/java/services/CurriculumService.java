package services;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import domain.Curriculum;
import domain.EducationData;
import domain.Mark;
import domain.MiscellaneousData;
import domain.PersonalData;
import domain.PositionData;
import repositories.CurriculumRepository;

@Service
@Transactional
public class CurriculumService {

	@Autowired
	private CurriculumRepository curriculumRepository;

	@Autowired
	private PersonalDataService personalDataService;

	@Autowired
	private PositionDataService positionDataService;

	@Autowired
	private MiscellaneousDataService miscellaneousDataService;

	@Autowired
	private EducationDataService educationDataService;

	public Curriculum findOne(int id) {
		return curriculumRepository.findOne(id);
	}

	public Curriculum save(Curriculum curriculum) {
		return curriculumRepository.save(curriculum);
	}

	public Curriculum copyCurriculum(Curriculum curriculum) {
		Curriculum copy = new Curriculum();
		PersonalData copyP = new PersonalData();
		List<PositionData> copyPos = new ArrayList<PositionData>();
		List<MiscellaneousData> copyMis = new ArrayList<MiscellaneousData>();
		List<EducationData> copyEd = new ArrayList<EducationData>();

		PositionData pos = new PositionData();
		MiscellaneousData mis = new MiscellaneousData();
		EducationData edu = new EducationData();

		copyP.setFullName(curriculum.getPersonalData().getFullName());
		copyP.setGitHubProfile(curriculum.getPersonalData().getGitHubProfile());
		copyP.setLinkedinProfile(curriculum.getPersonalData().getLinkedinProfile());
		copyP.setPhoneNumber(curriculum.getPersonalData().getPhoneNumber());
		copyP.setStatement(curriculum.getPersonalData().getStatement());
		this.personalDataService.save(copyP);

		for (PositionData p : curriculum.getPositionData()) {
			pos.setDescription(p.getDescription());
			pos.setEndDate(p.getEndDate());
			pos.setStartDate(p.getStartDate());
			pos.setTitle(p.getTitle());

			this.positionDataService.save(pos);
			copyPos.add(pos);
			pos = new PositionData();
		}

		for (MiscellaneousData p : curriculum.getMiscellaneousData()) {
			mis.setAttachments(p.getAttachments());
			mis.setFreeText(p.getFreeText());

			this.miscellaneousDataService.save(mis);
			copyMis.add(mis);
			mis = new MiscellaneousData();
		}

		for (EducationData p : curriculum.getEducationData()) {
			edu.setDegree(p.getDegree());
			edu.setEndDate(p.getEndDate());
			edu.setInstitution(p.getInstitution());
			edu.setStartDate(p.getStartDate());
			edu.setMark(p.getMark());

			this.educationDataService.save(edu);
			copyEd.add(edu);
			edu = new EducationData();
		}

		copy.setTitle(curriculum.getTitle());
		copy.setPositionData(copyPos);
		copy.setPersonalData(copyP);
		copy.setMiscellaneousData(copyMis);
		copy.setEducationData(copyEd);
		
		Curriculum saved = this.curriculumRepository.save(copy);

		return saved;
	}

}
