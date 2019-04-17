package services;

import java.util.List;
import java.util.ArrayList;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;

import repositories.CurriculumRepository;
import domain.Curriculum;
import domain.EducationData;
import domain.Hacker;
import domain.MiscellaneousData;
import domain.PersonalData;
import domain.PositionData;
import forms.FormObjectCurriculumPersonalData;

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

	@Autowired
	private HackerService hackerService;

	public Curriculum findOne(int id) {
		return this.curriculumRepository.findOne(id);
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

	public List<Curriculum> getCurriculumsOfHacker(int hackerId) {
		return this.curriculumRepository.getCurriculumsOfHacker(hackerId);
	}

	public List<Curriculum> getCurriculumsOfLoggedHacker() {
		Hacker hacker = this.hackerService.securityAndHacker();
		List<Curriculum> curriculums = this.getCurriculumsOfHacker(hacker.getId());

		return curriculums;
	}

	public Curriculum getCurriculumOfHacker(int hackerId, int curriculumId) {
		return this.curriculumRepository.getCurriculumOfHacker(hackerId, curriculumId);
	}

	public Curriculum getCurriculumOfLoggedHacker(int curriculumId) {
		Hacker hacker = this.hackerService.securityAndHacker();
		Curriculum curriculum = this.getCurriculumOfHacker(hacker.getId(), curriculumId);
		Assert.notNull(curriculum);
		return curriculum;
	}

	public Curriculum reconstruct(FormObjectCurriculumPersonalData formObject, BindingResult binding,
			PersonalData personalData) {
		Curriculum curriculum = new Curriculum();

		if (formObject.getId() > 0) {
			Curriculum curriculumFounded = this.findOne(formObject.getId());

			curriculum.setId(curriculumFounded.getId());
			curriculum.setVersion(curriculumFounded.getVersion());
		}
		curriculum.setTitle(formObject.getTitle());
		curriculum.setPersonalData(personalData);

		return curriculum;
	}

	public Curriculum save(Curriculum curriculum) {
		return this.curriculumRepository.save(curriculum);
	}

	public void delete(Curriculum curriculum) {
		this.curriculumRepository.delete(curriculum);
	}

	public void deleteCurriculumAsHacker(int curriculumId) {
		Hacker hacker = this.hackerService.securityAndHacker();

		Curriculum curriculum = this.getCurriculumOfHacker(hacker.getId(), curriculumId);
		Assert.notNull(curriculum);

		List<Curriculum> curriculums = hacker.getCurriculums();

		curriculums.remove(curriculum);
		hacker.setCurriculums(curriculums);
		this.hackerService.save(hacker);

		this.delete(curriculum);
	}

	public Curriculum getCurriculumOfPositionData(int positionDataId) {
		return this.curriculumRepository.getCurriculumOfPositionData(positionDataId);
	}

	public Curriculum getCurriculumOfEducationData(int educationDataId) {
		return this.curriculumRepository.getCurriculumOfEducationData(educationDataId);
	}

	public List<Curriculum> findAll() {
		return this.curriculumRepository.findAll();
	}

	public Curriculum getCurriculumOfMiscellaneousData(int miscellaneousDataId) {
		return this.curriculumRepository.getCurriculumOfMiscellaneousData(miscellaneousDataId);
	}

	public String curriculumToStringExport() {
		String res = "";
		Hacker hacker = this.hackerService.loggedHacker();
		List<Curriculum> curriculums = new ArrayList<Curriculum>();
		StringBuilder sb = new StringBuilder();
		curriculums = hacker.getCurriculums();

		/*
		 * + c.getTitle() + " EducationalData: " + c.getEducationData().toString() +
		 * " Miscellaneous data: " + c.getMiscellaneousData().toString() +
		 * "Position data" + c.getPositionData().toString(
		 */

		Integer cont = 1;

		for (Curriculum c : curriculums) {
			sb.append("Curriculum" + cont + ": ").append(System.getProperty("line.separator"));
			sb.append("Title: " + c.getTitle()).append(System.getProperty("line.separator"));
			sb.append("Full name: " + c.getPersonalData().getFullName()).append(System.getProperty("line.separator"));
			sb.append("GitHub profile: " + c.getPersonalData().getGitHubProfile())
					.append(System.getProperty("line.separator"));
			sb.append("Linkedin profile: " + c.getPersonalData().getLinkedinProfile())
					.append(System.getProperty("line.separator"));
			sb.append("Statement: " + c.getPersonalData().getStatement()).append(System.getProperty("line.separator"));
			sb.append("Phone number: " + c.getPersonalData().getPhoneNumber())
					.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));

			sb.append("Titles of Positions data: " + this.curriculumRepository.getTitlesOfPositionDatas(hacker))
					.append(System.getProperty("line.separator"));

			sb.append(System.getProperty("line.separator"));

			sb.append("Degrees of Educations data: " + this.curriculumRepository.getDegreesOfEducationalData(hacker))
					.append(System.getProperty("line.separator"));

			sb.append(System.getProperty("line.separator"));

			sb.append("Free texts of Miscellaneous data: "
					+ this.curriculumRepository.getFreeTestOfMiscellaneousData(hacker))
					.append(System.getProperty("line.separator"));

			sb.append(System.getProperty("line.separator"));

			cont++;
		}
		return sb.toString();
	}

	public void flush() {
		this.curriculumRepository.flush();
	}
	
	public void deleteInBatch(List<Curriculum> curriculums) {
		this.curriculumRepository.deleteInBatch(curriculums);
	}
}
