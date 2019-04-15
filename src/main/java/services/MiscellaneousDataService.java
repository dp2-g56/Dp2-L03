package services;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import domain.Curriculum;
import domain.Hacker;
import domain.MiscellaneousData;
import domain.PersonalData;
import domain.PositionData;
import repositories.CurriculumRepository;
import repositories.MiscellaneousDataRepository;

@Service
@Transactional
public class MiscellaneousDataService {
	
	@Autowired
	private MiscellaneousDataRepository miscellaneousDataRepository;
	@Autowired
	private HackerService hackerService;
	@Autowired
	private CurriculumService curriculumService;
	@Autowired
	private Validator validator;
	
	public void save(MiscellaneousData m) {
		this.miscellaneousDataRepository.save(m);
	}
	
	public MiscellaneousData findOne(int miscellaneousDataId) {
		return this.miscellaneousDataRepository.findOne(miscellaneousDataId);
	}

	public MiscellaneousData getMiscellaneousDataOfLoggedHacker(int miscellaneousDataId) {
		Hacker hacker = this.hackerService.securityAndHacker();
		MiscellaneousData miscellaneousData = this.findOne(miscellaneousDataId);
		Assert.isTrue(this.miscellaneousDataRepository.getMiscellaneousDataOfHacker(hacker.getId()).contains(miscellaneousData));
		return miscellaneousData;
	}
	
	public void deleteInBatch(Iterable<MiscellaneousData> entities) {
		this.miscellaneousDataRepository.deleteInBatch(entities);
	}

	public void addOrUpdateMiscellaneousDataAsHacker(MiscellaneousData miscellaneousData, int curriculumId) {
		Hacker hacker = this.hackerService.securityAndHacker();
		
		if(miscellaneousData.getId()==0) {
			Curriculum curriculum = this.curriculumService.getCurriculumOfHacker(hacker.getId(), curriculumId);
			List<MiscellaneousData> miscellaneoussData = curriculum.getMiscellaneousData();
			miscellaneoussData.add(miscellaneousData);
			this.curriculumService.save(curriculum);
		} else {
			this.save(miscellaneousData);
		}
	}

	public MiscellaneousData reconstruct(MiscellaneousData miscellaneousData, BindingResult binding) {
		MiscellaneousData miscellaneousDataReconstruct = new MiscellaneousData();
		
		if(miscellaneousData.getId()==0) {
			miscellaneousDataReconstruct.setFreeText(miscellaneousData.getFreeText());
			miscellaneousDataReconstruct.setAttachments(new ArrayList<String>());
		} else {
			MiscellaneousData miscellaneousDataFounded = this.findOne(miscellaneousData.getId());
			miscellaneousDataReconstruct.setId(miscellaneousDataFounded.getId());
			miscellaneousDataReconstruct.setVersion(miscellaneousDataFounded.getVersion());
			miscellaneousDataReconstruct.setFreeText(miscellaneousData.getFreeText());
			miscellaneousDataReconstruct.setAttachments(miscellaneousDataFounded.getAttachments());
		}
		
		this.validator.validate(miscellaneousDataReconstruct, binding);
		
		return miscellaneousDataReconstruct;
	}

	public void deleteMiscellaneousDataAsHacker(int miscellaneousDataId) {
		Hacker hacker = this.hackerService.securityAndHacker();
		Assert.notNull(this.miscellaneousDataRepository.getMiscellaneousDataOfHacker(hacker.getId(), miscellaneousDataId));
		MiscellaneousData miscellaneousData = this.findOne(miscellaneousDataId);
		Curriculum curriculum = this.curriculumService.getCurriculumOfMiscellaneousData(miscellaneousDataId);
		List<MiscellaneousData> miscellaneoussData = curriculum.getMiscellaneousData();
		miscellaneoussData.remove(miscellaneousData);
		curriculum.setMiscellaneousData(miscellaneoussData);
		this.curriculumService.save(curriculum);
		this.miscellaneousDataRepository.delete(miscellaneousData);
	}

	public void addAttachmentAsHacker(int miscellaneousDataId, String attachment) {
		Hacker hacker = this.hackerService.securityAndHacker();
		MiscellaneousData miscellaneousData = this.miscellaneousDataRepository.getMiscellaneousDataOfHacker(hacker.getId(), miscellaneousDataId);
		Assert.notNull(miscellaneousData);
		Assert.isTrue(!attachment.contentEquals(""));
		List<String> attachments = miscellaneousData.getAttachments();
		attachments.add(attachment);
		this.save(miscellaneousData);
	}
}
