package services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import domain.Curriculum;
import domain.Hacker;
import domain.MiscellaneousData;
import repositories.CurriculumRepository;
import repositories.MiscellaneousDataRepository;

@Service
@Transactional
public class MiscellaneousDataService {
	
	@Autowired
	private MiscellaneousDataRepository miscellaneousDataRepository;
	@Autowired
	private HackerService hackerService;
	
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
}
