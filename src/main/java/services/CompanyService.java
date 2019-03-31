
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


	//----------------------------------------CRUD METHODS--------------------------
	//------------------------------------------------------------------------------
	public Company save(Company company) {
		return this.companyRepository.save(company);
	}

	//-----------------------------------------SECURITY-----------------------------
	//------------------------------------------------------------------------------

	/**
	 * LoggedCompany now contains the security of loggedAsCompany
	 * 
	 * @return
	 */
	public Company loggedCompany() {
		Company company = new Company();
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		List<Authority> authorities = (List<Authority>) userAccount.getAuthorities();
		Assert.isTrue(authorities.get(0).toString().equals("COMPANY"));
		company = this.companyRepository.getCompanyByUsername(userAccount.getUsername());
		Assert.notNull(company);
		return company;
	}

}
