
package services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.CompanyRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Company;

@Service
@Transactional
public class CompanyService {

	@Autowired
	private CompanyRepository	companyRepository;


	public void loggedAsCompany() {

		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		List<Authority> authorities = (List<Authority>) userAccount.getAuthorities();
		Assert.isTrue(authorities.get(0).toString().equals("COMPANY"));

	}

	public Company getLoggedCompany() {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		return this.companyRepository.getCompanyByUsername(userAccount.getUsername());
	}
}
