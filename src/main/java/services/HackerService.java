
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
import domain.Hacker;
import domain.Problem;

@Service
@Transactional
public class HackerService {

	@Autowired
	private HackerRepository hackerRepository;

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

	public void save(Hacker hacker) {
		this.loggedAsHacker();
		this.hackerRepository.save(hacker);
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

}
