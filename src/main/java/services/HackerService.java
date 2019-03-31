
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
import domain.Company;
import domain.Hacker;
import domain.Problem;

@Service
@Transactional
public class HackerService {

	@Autowired
	private HackerRepository hackerRepository;

	// Auxiliar methods
	public Hacker securityAndHacker() {
		UserAccount userAccount = LoginService.getPrincipal();
		String username = userAccount.getUsername();

		Hacker loggedHacker = this.hackerRepository.getHackerByUsername(username);
		List<Authority> authorities = (List<Authority>) loggedHacker.getUserAccount().getAuthorities();
		Assert.isTrue(authorities.get(0).toString().equals("HACKER"));

		return loggedHacker;
	}
	
}
