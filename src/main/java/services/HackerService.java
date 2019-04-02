
package services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.CompanyRepository;
import repositories.HackerRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Application;
import domain.Company;
import domain.Curriculum;
import domain.Hacker;
import domain.Problem;

@Service
@Transactional
public class HackerService {

	@Autowired
	private HackerRepository hackerRepository;
	@Autowired
	private CurriculumService curriculumService;

	@Autowired
	private ApplicationService applicationService;

	// Auxiliar methods
	public Hacker securityAndHacker() {
		UserAccount userAccount = LoginService.getPrincipal();
		String username = userAccount.getUsername();

		Hacker loggedHacker = this.hackerRepository.getHackerByUsername(username);
		List<Authority> authorities = (List<Authority>) loggedHacker.getUserAccount().getAuthorities();
		Assert.isTrue(authorities.get(0).toString().equals("HACKER"));

		return loggedHacker;
	}

	public Hacker loggedHacker() {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		List<Authority> authorities = (List<Authority>) userAccount.getAuthorities();
		Assert.isTrue(authorities.get(0).toString().equals("HACKER"));
		return this.hackerRepository.getHackerByUsername(userAccount.getUsername());
	}

	public void loggedAsHacker() {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		List<Authority> authorities = (List<Authority>) userAccount.getAuthorities();
		Assert.isTrue(authorities.get(0).toString().equals("HACKER"));

	}

	public void addApplication(Application p) {
		Hacker hacker = this.loggedHacker();
		Assert.isTrue(p.getId() == 0);

		p.setHacker(hacker);
		Application application = this.applicationService.save(p);
		List<Application> applications = hacker.getApplications();
		applications.add(application);
		hacker.setApplications(applications);
		this.save(hacker);
	}
	
	public Hacker getHackerByUsername(String username) {
		return this.hackerRepository.getHackerByUsername(username);
	}

	public Hacker save(Hacker hacker) {
		return this.hackerRepository.save(hacker);
	}

	public void addCurriculum(Curriculum curriculum) {
		Hacker hacker = this.securityAndHacker();
		
		if(curriculum.getId() > 0) {
			Assert.isTrue(hacker.getCurriculums().contains(this.curriculumService.findOne(curriculum.getId())));
			this.curriculumService.save(curriculum);
		} else {
			List<Curriculum> curriculums = hacker.getCurriculums();
			curriculums.add(curriculum);
			hacker.setCurriculums(curriculums);
			this.save(hacker);
		}
	}
}
