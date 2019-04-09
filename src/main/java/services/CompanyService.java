
package services;

import java.util.ArrayList;
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
import domain.Position;
import domain.Problem;

@Service
@Transactional
public class CompanyService {

	@Autowired
	private CompanyRepository	companyRepository;

	@Autowired
	private ProblemService		problemService;


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

		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		List<Authority> authorities = (List<Authority>) userAccount.getAuthorities();
		return this.companyRepository.getCompanyByUsername(userAccount.getUsername());
	}

	public void loggedAsCompany() {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		List<Authority> authorities = (List<Authority>) userAccount.getAuthorities();
		Assert.isTrue(authorities.get(0).toString().equals("COMPANY"));

	}

	public void addProblem(Problem p) {
		Company loggedCompany = this.loggedCompany();
		Assert.isTrue(p.getId() == 0);

		Problem problem = this.problemService.save(p);
		List<Problem> problems = loggedCompany.getProblems();
		problems.add(problem);
		loggedCompany.setProblems(problems);
		this.save(loggedCompany);
	}

	public Company findOne(int idCompany) {
		return this.companyRepository.findOne(idCompany);
	}

	public List<Position> AllPositionsInFinal() {
		List<Position> finalPositions = new ArrayList<Position>();

		finalPositions = this.companyRepository.positionsInFinal();

		return finalPositions;
	}

	public Company companyOfRespectivePosition(int idPosition) {
		Company company = new Company();
		company = this.companyRepository.companyByPosition(idPosition);

		return company;
	}

	public List<Company> allCompanies() {
		List<Company> allCompaniesList = new ArrayList<Company>();
		allCompaniesList = this.companyRepository.allCompanies();

		return allCompaniesList;

	}

	public List<Position> positionOfRespectiveCompany(int idCompany) {
		List<Position> positions = new ArrayList<Position>();

		positions = this.companyRepository.positionsOfCompany(idCompany);

		return positions;

	}
}
