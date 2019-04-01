
package services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import domain.Curriculum;
import domain.Hacker;
import repositories.CurriculumRepository;

@Service
@Transactional
public class CurriculumService {

	@Autowired
	private CurriculumRepository curriculumRepository;
	@Autowired
	private HackerService hackerService;
	
	public List<Curriculum> getCurriculumsOfHacker(int hackerId) {
		return this.curriculumRepository.getCurriculumsOfHacker(hackerId);
	}

	public List<Curriculum> getCurriculumsOfLoggedHacker() {
		Hacker hacker = this.hackerService.securityAndHacker();
		List<Curriculum> curriculums = this.getCurriculumsOfHacker(hacker.getId());
		
		return curriculums;
	}

	public Curriculum findOne(int curriculumId) {
		return this.curriculumRepository.findOne(curriculumId);
	}

	public Curriculum getCurriculumOfLoggedHacker(int curriculumId) {
		Hacker hacker = this.hackerService.securityAndHacker();
		Curriculum curriculum = this.findOne(curriculumId);
		Assert.isTrue(hacker.getCurriculums().contains(curriculum));
		return curriculum;
	}
	
}
