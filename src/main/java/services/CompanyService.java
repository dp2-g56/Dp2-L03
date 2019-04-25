
package services;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;

import repositories.CompanyRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Application;
import domain.Company;
import domain.CreditCard;
import domain.Curriculum;
import domain.Hacker;
import domain.Message;
import domain.Position;
import domain.Problem;
import domain.SocialProfile;
import forms.FormObjectCompany;
import forms.FormObjectEditCompany;

@Service
@Transactional
public class CompanyService {

	@Autowired
	private CompanyRepository		companyRepository;

	@Autowired
	private ProblemService			problemService;

	@Autowired
	private CreditCardService		creditCardService;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private ApplicationService		applicationService;

	@Autowired
	private PositionService			positionService;

	@Autowired
	private FinderService			finderService;

	@Autowired
	private CurriculumService		curriculumService;

	@Autowired
	private Validator				validator;


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
		this.loggedAsCompany();
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

	public Company createCompany() {
		Company company = new Company();
		CreditCard card = new CreditCard();

		// Se crean las listas vacias
		// ACTOR
		List<SocialProfile> socialProfiles = new ArrayList<SocialProfile>();
		List<Message> messages = new ArrayList<Message>();
		List<Problem> problems = new ArrayList<Problem>();
		List<Position> positions = new ArrayList<Position>();

		UserAccount userAccount = new UserAccount();
		userAccount.setUsername("");
		userAccount.setPassword("");

		// Actor
		company.setProblems(problems);
		company.setPositions(positions);

		company.setAddress("");
		company.setEmail("");
		company.setCreditCard(card);
		company.setMessages(messages);
		company.setHasSpam(false);
		company.setName("");
		company.setPhone("");
		company.setPhoto("");
		company.setSocialProfiles(socialProfiles);
		company.setSurname("");
		company.setVATNumber("");
		company.setCompanyName("");

		List<Authority> authorities = new ArrayList<Authority>();

		Authority authority = new Authority();
		authority.setAuthority(Authority.COMPANY);
		authorities.add(authority);

		userAccount.setAuthorities(authorities);
		userAccount.setIsNotLocked(true);

		company.setUserAccount(userAccount);

		return company;

	}

	public Company reconstruct(FormObjectCompany formObjectCompany, BindingResult binding) {

		Company result = this.createCompany();

		result.setAddress(formObjectCompany.getAddress());
		result.setEmail(formObjectCompany.getEmail());
		result.setHasSpam(false);
		result.setName(formObjectCompany.getName());
		result.setPhone(formObjectCompany.getPhone());
		result.setPhoto(formObjectCompany.getPhoto());
		result.setSurname(formObjectCompany.getSurname());
		result.setVATNumber(formObjectCompany.getVATNumber());
		result.setCompanyName(formObjectCompany.getCompanyName());

		CreditCard card = new CreditCard();

		card.setBrandName(formObjectCompany.getBrandName());
		card.setHolderName(formObjectCompany.getHolderName());
		card.setNumber(formObjectCompany.getNumber());
		card.setExpirationMonth(formObjectCompany.getExpirationMonth());
		card.setExpirationYear(formObjectCompany.getExpirationYear());
		card.setCvvCode(formObjectCompany.getCvvCode());

		result.setCreditCard(card);

		// USER ACCOUNT
		UserAccount userAccount = new UserAccount();

		// Authorities
		List<Authority> authorities = new ArrayList<Authority>();
		Authority authority = new Authority();
		authority.setAuthority(Authority.COMPANY);
		authorities.add(authority);
		userAccount.setAuthorities(authorities);

		// locked
		userAccount.setIsNotLocked(true);

		// Username
		userAccount.setUsername(formObjectCompany.getUsername());

		// Password
		Md5PasswordEncoder encoder;
		encoder = new Md5PasswordEncoder();
		userAccount.setPassword(encoder.encodePassword(formObjectCompany.getPassword(), null));

		result.setUserAccount(userAccount);

		String locale = LocaleContextHolder.getLocale().getLanguage().toUpperCase();

		//Confirmacion contrasena
		if (!formObjectCompany.getPassword().equals(formObjectCompany.getConfirmPassword())) {
			if (locale.contains("ES")) {
				binding.addError(new FieldError("formObjectCompany", "password", formObjectCompany.getPassword(), false, null, null, "Las contrasenas no coinciden"));
			} else {
				binding.addError(new FieldError("formObjectCompany", "password", formObjectCompany.getPassword(), false, null, null, "Passwords don't match"));
			}
		}

		//Confirmacion terminos y condiciones
		if (!formObjectCompany.getTermsAndConditions()) {
			if (locale.contains("ES")) {
				binding.addError(new FieldError("formObjectCompany", "termsAndConditions", formObjectCompany.getTermsAndConditions(), false, null, null, "Debe aceptar los terminos y condiciones"));
			} else {
				binding.addError(new FieldError("formObjectCompany", "termsAndConditions", formObjectCompany.getTermsAndConditions(), false, null, null, "You must accept the terms and conditions"));
			}
		}

		if (card.getNumber() != null) {
			if (!this.creditCardService.validateNumberCreditCard(card)) {
				if (LocaleContextHolder.getLocale().getLanguage().toUpperCase().contains("ES")) {
					binding.addError(new FieldError("formObject", "number", formObjectCompany.getNumber(), false, null, null, "El numero de la tarjeta es invalido"));
				} else {
					binding.addError(new FieldError("formObject", "number", formObjectCompany.getNumber(), false, null, null, "The card number is invalid"));
				}
			}
		}

		if (card.getExpirationMonth() != null && card.getExpirationYear() != null) {
			if (!this.creditCardService.validateDateCreditCard(card)) {
				if (LocaleContextHolder.getLocale().getLanguage().toUpperCase().contains("ES")) {
					binding.addError(new FieldError("formObject", "expirationMonth", card.getExpirationMonth(), false, null, null, "La tarjeta no puede estar caducada"));
				} else {
					binding.addError(new FieldError("formObject", "expirationMonth", card.getExpirationMonth(), false, null, null, "The credit card can not be expired"));
				}
			}
		}

		List<String> cardType = this.configurationService.getConfiguration().getCardType();

		if (!cardType.contains(result.getCreditCard().getBrandName())) {
			if (LocaleContextHolder.getLocale().getLanguage().toUpperCase().contains("ES")) {
				binding.addError(new FieldError("formObject", "brandName", card.getBrandName(), false, null, null, "Tarjeta no admitida"));
			} else {
				binding.addError(new FieldError("formObject", "brandName", card.getBrandName(), false, null, null, "The credit card is not accepted"));
			}
		}

		if (result.getEmail().matches("[\\w.%-]+\\<[\\w.%-]+\\@+\\>|[\\w.%-]+")) {
			if (LocaleContextHolder.getLocale().getLanguage().toUpperCase().contains("ES")) {
				binding.addError(new FieldError("formObjectCompany", "email", result.getEmail(), false, null, null, "No sigue el patron ejemplo@dominio.asd o alias <ejemplo@dominio.asd>"));
			} else {
				binding.addError(new FieldError("formObjectCompany", "email", result.getEmail(), false, null, null, "Dont follow the pattern example@domain.asd or alias <example@domain.asd>"));
			}
		}

		return result;
	}

	public FormObjectEditCompany getFormObjectEditCompany(Company company) {

		FormObjectEditCompany res = new FormObjectEditCompany();

		//Company
		res.setAddress(company.getAddress());
		res.setName(company.getName());
		res.setVATNumber(company.getVATNumber());
		res.setPhoto(company.getPhoto());
		res.setEmail(company.getEmail());
		res.setAddress(company.getAddress());
		res.setCompanyName(company.getCompanyName());
		res.setSurname(company.getSurname());
		res.setPhone(company.getPhone());

		//Credit Card
		CreditCard c = company.getCreditCard();

		res.setHolderName(c.getHolderName());
		res.setBrandName(c.getBrandName());
		res.setNumber(c.getNumber());
		res.setExpirationMonth(c.getExpirationMonth());
		res.setExpirationYear(c.getExpirationYear());
		res.setCvvCode(c.getCvvCode());

		return res;

	}

	public Company reconstructCompanyPersonalData(FormObjectEditCompany formObjectCompany, BindingResult binding) {
		Company res = new Company();

		Company companyDB = this.findOne(formObjectCompany.getId());

		CreditCard card = new CreditCard();

		//Credit Card
		card.setBrandName(formObjectCompany.getBrandName());
		card.setCvvCode(formObjectCompany.getCvvCode());
		card.setExpirationMonth(formObjectCompany.getExpirationMonth());
		card.setExpirationYear(formObjectCompany.getExpirationYear());
		card.setHolderName(formObjectCompany.getHolderName());
		card.setNumber(formObjectCompany.getNumber());

		res.setAddress(formObjectCompany.getAddress());
		res.setCompanyName(formObjectCompany.getCompanyName());
		res.setEmail(formObjectCompany.getEmail());
		res.setPhone(formObjectCompany.getPhone());
		res.setPhoto(formObjectCompany.getPhoto());
		res.setSurname(formObjectCompany.getSurname());
		res.setName(formObjectCompany.getName());
		res.setId(formObjectCompany.getId());
		res.setVATNumber(formObjectCompany.getVATNumber());
		res.setCreditCard(card);
		res.setPositions(companyDB.getPositions());
		res.setProblems(companyDB.getProblems());
		res.setHasSpam(companyDB.getHasSpam());
		res.setMessages(companyDB.getMessages());
		res.setSocialProfiles(companyDB.getSocialProfiles());
		res.setUserAccount(companyDB.getUserAccount());
		res.setVersion(companyDB.getVersion());

		if (card.getNumber() != null) {
			if (!this.creditCardService.validateNumberCreditCard(card)) {
				if (LocaleContextHolder.getLocale().getLanguage().toUpperCase().contains("ES")) {
					binding.addError(new FieldError("formObject", "number", formObjectCompany.getNumber(), false, null, null, "El numero de la tarjeta es invalido"));
				} else {
					binding.addError(new FieldError("formObject", "number", formObjectCompany.getNumber(), false, null, null, "The card number is invalid"));
				}
			}
		}

		if (card.getExpirationMonth() != null && card.getExpirationYear() != null) {
			if (!this.creditCardService.validateDateCreditCard(card)) {
				if (LocaleContextHolder.getLocale().getLanguage().toUpperCase().contains("ES")) {
					binding.addError(new FieldError("formObject", "expirationMonth", card.getExpirationMonth(), false, null, null, "La tarjeta no puede estar caducada"));
				} else {
					binding.addError(new FieldError("formObject", "expirationMonth", card.getExpirationMonth(), false, null, null, "The credit card can not be expired"));
				}
			}
		}

		List<String> cardType = this.configurationService.getConfiguration().getCardType();

		if (!cardType.contains(res.getCreditCard().getBrandName())) {
			if (LocaleContextHolder.getLocale().getLanguage().toUpperCase().contains("ES")) {
				binding.addError(new FieldError("formObject", "brandName", card.getBrandName(), false, null, null, "Tarjeta no admitida"));
			} else {
				binding.addError(new FieldError("formObject", "brandName", card.getBrandName(), false, null, null, "The credit card is not accepted"));
			}
		}

		if (res.getEmail().matches("[\\w.%-]+\\<[\\w.%-]+\\@+\\>|[\\w.%-]+")) {
			if (LocaleContextHolder.getLocale().getLanguage().toUpperCase().contains("ES")) {
				binding.addError(new FieldError("formObjectCompany", "email", res.getEmail(), false, null, null, "No sigue el patron ejemplo@dominio.asd o alias <ejemplo@dominio.asd>"));
			} else {
				binding.addError(new FieldError("formObjectCompany", "email", res.getEmail(), false, null, null, "Dont follow the pattern example@domain.asd or alias <example@domain.asd>"));
			}
		}

		return res;

	}

	public void updateCompany(Company company) {
		this.loggedAsCompany();
		Assert.isTrue(company.getId() == this.loggedCompany().getId());

		this.save(company);
	}

	public void deleteCompany() {
		this.loggedAsCompany();
		Company company = this.loggedCompany();

		int companyId = company.getId();

		List<Application> applications = this.companyRepository.applicationsOfCompany(companyId);

		List<Hacker> hackers = this.companyRepository.hackersOfCompany(companyId);

		List<Curriculum> curriculums = this.companyRepository.curriculumsOfApplicationssOfCompany(companyId);

		for (Hacker h : hackers) {
			h.getApplications().removeAll(applications);
		}

		this.applicationService.deleteinBatch(applications);

		this.curriculumService.deleteInBatch(curriculums);

		List<Position> positions = new ArrayList<Position>();
		positions.addAll(company.getPositions());

		List<Problem> problems = new ArrayList<Problem>();
		problems.addAll(company.getProblems());

		company.getProblems().clear();
		company.getPositions().clear();

		this.finderService.cleanFindersOfPositions(positions);

		for (Position p : positions) {
			this.positionService.delete(p);
		}

		for (Problem pr : problems) {
			this.problemService.delete(pr);
		}

		this.companyRepository.delete(company);

	}

	public void flush() {
		this.companyRepository.flush();

	}

	public Company getCompanyByUsername(String username) {
		return this.companyRepository.getCompanyByUsername(username);
	}

	public List<Position> positionsOfCompanyInFinalNotCancelled(int idCompany) {
		return this.companyRepository.positionsOfCompanyInFinalNotCancelled(idCompany);
	}
}
