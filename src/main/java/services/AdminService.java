
package services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

import repositories.AdminRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Admin;
import domain.Company;
import domain.CreditCard;
import domain.Hacker;
import domain.Message;
import domain.Position;
import domain.SocialProfile;
import forms.FormObjectAdmin;
import forms.FormObjectEditAdmin;

@Service
@Transactional
public class AdminService {

	@Autowired
	private AdminRepository			adminRepository;

	@Autowired
	private CreditCardService		creditCardService;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private FinderService			finderService;

	@Autowired
	private MessageService			messageService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private Validator				validator;


	// ----------------------------------------CRUD
	// METHODS--------------------------
	// ------------------------------------------------------------------------------

	public Admin save(Admin admin) {
		return this.adminRepository.save(admin);
	}

	// -----------------------------------------SECURITY-----------------------------
	// ------------------------------------------------------------------------------

	/**
	 * LoggedAdmin now contains the security of loggedAsAdmin
	 * 
	 * @return
	 */
	public Admin loggedAdmin() {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		List<Authority> authorities = (List<Authority>) userAccount.getAuthorities();
		Assert.isTrue(authorities.get(0).toString().equals("ADMIN"));
		return this.adminRepository.getAdminByUsername(userAccount.getUsername());
	}

	public void loggedAsAdmin() {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		List<Authority> authorities = (List<Authority>) userAccount.getAuthorities();
		Assert.isTrue(authorities.get(0).toString().equals("ADMIN"));

	}

	public Admin createAdmin() {

		Admin admin = new Admin();
		CreditCard card = new CreditCard();

		// Se crean las listas vacias
		// ACTOR
		List<SocialProfile> socialProfiles = new ArrayList<SocialProfile>();
		List<Message> messages = new ArrayList<Message>();

		UserAccount userAccount = new UserAccount();
		userAccount.setUsername("");
		userAccount.setPassword("");

		// Actor
		admin.setAddress("");
		admin.setEmail("");
		admin.setCreditCard(card);
		admin.setMessages(messages);
		admin.setHasSpam(false);
		admin.setName("");
		admin.setPhone("");
		admin.setPhoto("");
		admin.setSocialProfiles(socialProfiles);
		admin.setSurname("");
		admin.setVATNumber("");

		List<Authority> authorities = new ArrayList<Authority>();

		Authority authority = new Authority();
		authority.setAuthority(Authority.ADMIN);
		authorities.add(authority);

		userAccount.setAuthorities(authorities);
		userAccount.setIsNotLocked(true);

		admin.setUserAccount(userAccount);

		return admin;
	}

	public Admin reconstruct(FormObjectAdmin formObjectAdmin, BindingResult binding) {

		Admin result = this.createAdmin();

		result.setAddress(formObjectAdmin.getAddress());
		result.setEmail(formObjectAdmin.getEmail());
		result.setHasSpam(false);
		result.setName(formObjectAdmin.getName());
		result.setPhone(formObjectAdmin.getPhone());
		result.setPhoto(formObjectAdmin.getPhoto());
		result.setSurname(formObjectAdmin.getSurname());
		result.setVATNumber(formObjectAdmin.getVATNumber());

		CreditCard card = new CreditCard();

		card.setBrandName(formObjectAdmin.getBrandName());
		card.setHolderName(formObjectAdmin.getHolderName());
		card.setNumber(formObjectAdmin.getNumber());
		card.setExpirationMonth(formObjectAdmin.getExpirationMonth());
		card.setExpirationYear(formObjectAdmin.getExpirationYear());
		card.setCvvCode(formObjectAdmin.getCvvCode());

		result.setCreditCard(card);

		// USER ACCOUNT
		UserAccount userAccount = new UserAccount();

		// Authorities
		List<Authority> authorities = new ArrayList<Authority>();
		Authority authority = new Authority();
		authority.setAuthority(Authority.ADMIN);
		authorities.add(authority);
		userAccount.setAuthorities(authorities);

		// locked
		userAccount.setIsNotLocked(true);

		// Username
		userAccount.setUsername(formObjectAdmin.getUsername());

		// Password
		Md5PasswordEncoder encoder;
		encoder = new Md5PasswordEncoder();
		userAccount.setPassword(encoder.encodePassword(formObjectAdmin.getPassword(), null));

		result.setUserAccount(userAccount);

		String locale = LocaleContextHolder.getLocale().getLanguage().toUpperCase();

		// Confirmacion contrasena
		if (!formObjectAdmin.getPassword().equals(formObjectAdmin.getConfirmPassword()))
			if (locale.contains("ES"))
				binding.addError(new FieldError("formObjectAdmin", "password", formObjectAdmin.getPassword(), false, null, null, "Las contrasenas no coinciden"));
			else
				binding.addError(new FieldError("formObjectAdmin", "password", formObjectAdmin.getPassword(), false, null, null, "Passwords don't match"));

		// Confirmacion terminos y condiciones
		if (!formObjectAdmin.getTermsAndConditions())
			if (locale.contains("ES"))
				binding.addError(new FieldError("formObjectAdmin", "termsAndConditions", formObjectAdmin.getTermsAndConditions(), false, null, null, "Debe aceptar los terminos y condiciones"));
			else
				binding.addError(new FieldError("formObjectAdmin", "termsAndConditions", formObjectAdmin.getTermsAndConditions(), false, null, null, "You must accept the terms and conditions"));

		if (card.getNumber() != null)
			if (!this.creditCardService.validateNumberCreditCard(card))
				if (LocaleContextHolder.getLocale().getLanguage().toUpperCase().contains("ES"))
					binding.addError(new FieldError("formObject", "number", formObjectAdmin.getNumber(), false, null, null, "El numero de la tarjeta es invalido"));
				else
					binding.addError(new FieldError("formObject", "number", formObjectAdmin.getNumber(), false, null, null, "The card number is invalid"));

		if (card.getExpirationMonth() != null && card.getExpirationYear() != null)
			if (!this.creditCardService.validateDateCreditCard(card))
				if (LocaleContextHolder.getLocale().getLanguage().toUpperCase().contains("ES"))
					binding.addError(new FieldError("formObject", "expirationMonth", card.getExpirationMonth(), false, null, null, "La tarjeta no puede estar caducada"));
				else
					binding.addError(new FieldError("formObject", "expirationMonth", card.getExpirationMonth(), false, null, null, "The credit card can not be expired"));

		List<String> cardType = this.configurationService.getConfiguration().getCardType();

		if (!cardType.contains(result.getCreditCard().getBrandName()))
			if (LocaleContextHolder.getLocale().getLanguage().toUpperCase().contains("ES"))
				binding.addError(new FieldError("formObject", "brandName", card.getBrandName(), false, null, null, "Tarjeta no admitida"));
			else
				binding.addError(new FieldError("formObject", "brandName", card.getBrandName(), false, null, null, "The credit card is not accepted"));

		return result;
	}

	public void saveNewAdmin(Admin admin) {
		this.loggedAsAdmin();
		this.adminRepository.save(admin);
	}

	public List<Float> showStatisticsOfFinder() {

		List<Float> statistics = new ArrayList<Float>();

		Date thisMoment = new Date();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(thisMoment);
		calendar.add(Calendar.HOUR_OF_DAY, -this.configurationService.getConfiguration().getTimeFinder());

		if (this.adminRepository.numberNonEmptyFinders(calendar.getTime()) == 0) {

			statistics.add((float) 0);
			statistics.add((float) 0);
			statistics.add((float) 0);
			statistics.add((float) 0);

		} else {
			statistics.add(this.adminRepository.avgResultFinders(calendar.getTime()));
			statistics.add(this.adminRepository.minResultFinders(calendar.getTime()));
			statistics.add(this.adminRepository.maxResultFinders(calendar.getTime()));
			statistics.add(this.adminRepository.stddevResultFinders(calendar.getTime()));

		}

		if (this.adminRepository.ratioEmptyFinder(calendar.getTime()) == null)
			statistics.add((float) 0);
		else
			statistics.add(this.adminRepository.ratioEmptyFinder(calendar.getTime()));

		return statistics;

	}

	public List<Float> showStatisticsOfCurriculum() {

		List<Float> statistics = new ArrayList<Float>();

		statistics.add(this.adminRepository.avgCurriculumPerHacker());
		statistics.add(this.adminRepository.minCurriculumPerHacker());
		statistics.add(this.adminRepository.maxCurriculumPerHacker());
		statistics.add(this.adminRepository.stddevCurriculumPerHacker());

		return statistics;
	}

	public List<Float> showStatisticsOfPositionsPerCompany() {

		List<Float> statistics = new ArrayList<Float>();

		statistics.add(this.adminRepository.avgPositionsCompany());
		statistics.add(this.adminRepository.minPositionsCompany());
		statistics.add(this.adminRepository.maxPositionsCompany());
		statistics.add(this.adminRepository.stddevPositionsCompany());

		return statistics;

	}

	public List<Float> showStatisticsOfApplicationsPerHacker() {

		List<Float> statistics = new ArrayList<Float>();

		statistics.add(this.adminRepository.avgApplicationsHacker());
		statistics.add(this.adminRepository.minApplicationsHacker());
		statistics.add(this.adminRepository.maxApplicationsHacker());
		statistics.add(this.adminRepository.stddevApplicationsHacker());

		return statistics;

	}

	public List<Float> showStatisticsOfSalaries() {

		List<Float> statistics = new ArrayList<Float>();

		statistics.add(this.adminRepository.avgSalaries());
		statistics.add(this.adminRepository.minSalary());
		statistics.add(this.adminRepository.maxSalary());
		statistics.add(this.adminRepository.stddevSalaries());

		return statistics;

	}

	public List<Company> companiesMorePositions() {
		return this.adminRepository.companiesMorePositions();
	}

	public List<Hacker> hackersMoreApplications() {
		return this.adminRepository.hackersMoreApplications();
	}

	public List<Position> bestSalaryPositions() {
		return this.adminRepository.bestSalaryPositions();
	}

	public List<Position> worstSalaryPositions() {
		return this.adminRepository.worstSalaryPositions();
	}

	public void broadcastMessage(Message message) {
		Admin sender = this.loggedAdmin();

		String username = sender.getUserAccount().getUsername();

		List<Actor> actors = new ArrayList<Actor>();
		actors = this.actorService.allActorsExceptOne(username);

		Message message2 = this.messageService.createCopy(message.getSubject(), message.getBody(), message.getTags(), "BROADCAST");

		List<String> spamWords = new ArrayList<String>();
		spamWords = this.configurationService.getSpamWords();

		Boolean hasSpam = this.configurationService.isStringSpam(message.getBody(), spamWords) || this.configurationService.isStringSpam(message.getSubject(), spamWords) || this.configurationService.isStringSpam(message.getTags(), spamWords);

		if (hasSpam)
			message.setTags("SPAM");

		for (Actor a : actors) {

			message.setReceiver(a.getUserAccount().getUsername());
			a.getMessages().add(message);
			this.actorService.save(a);

		}
		sender.getMessages().add(message2);

		if (hasSpam)
			this.actorService.updateActorSpam(sender);

		this.save(sender);

	}

	public Message reconstruct(Message message, BindingResult binding) {

		String username = LoginService.getPrincipal().getUsername();

		domain.Message result;
		result = this.messageService.create();
		if (message.getId() == 0) {
			result = message;
			result.setSender(username);
			Date thisMoment = new Date();
			thisMoment.setTime(thisMoment.getTime() - 1000);
			result.setMoment(thisMoment);
			result.setReceiver(username);

		} else {
			result = this.messageService.findOne(message.getId());

			result.setBody(message.getBody());
			result.setTags(message.getTags());
			result.setSubject(message.getSubject());
			result.setReceiver(username);
			result.setSender(username);

		}

		this.validator.validate(result, binding);

		return result;

	}

	public FormObjectEditAdmin getFormObjectEditadmin(Admin admin) {

		FormObjectEditAdmin res = new FormObjectEditAdmin();

		//Company
		res.setAddress(admin.getAddress());
		res.setName(admin.getName());
		res.setVATNumber(admin.getVATNumber());
		res.setPhoto(admin.getPhoto());
		res.setEmail(admin.getEmail());
		res.setAddress(admin.getAddress());
		res.setSurname(admin.getSurname());
		res.setPhone(admin.getPhone());

		//Credit Card
		CreditCard c = admin.getCreditCard();

		res.setHolderName(c.getHolderName());
		res.setBrandName(c.getBrandName());
		res.setNumber(c.getNumber());
		res.setExpirationMonth(c.getExpirationMonth());
		res.setExpirationYear(c.getExpirationYear());
		res.setCvvCode(c.getCvvCode());

		return res;

	}

	public Admin reconstructCompanyPersonalData(FormObjectEditAdmin formObjectAdmin, BindingResult binding) {
		Admin res = new Admin();

		Admin adminDB = this.findOne(formObjectAdmin.getId());

		CreditCard card = new CreditCard();

		//Credit Card
		card.setBrandName(formObjectAdmin.getBrandName());
		card.setCvvCode(formObjectAdmin.getCvvCode());
		card.setExpirationMonth(formObjectAdmin.getExpirationMonth());
		card.setExpirationYear(formObjectAdmin.getExpirationYear());
		card.setHolderName(formObjectAdmin.getHolderName());
		card.setNumber(formObjectAdmin.getNumber());

		res.setAddress(formObjectAdmin.getAddress());

		res.setEmail(formObjectAdmin.getEmail());
		res.setPhone(formObjectAdmin.getPhone());
		res.setPhoto(formObjectAdmin.getPhoto());
		res.setSurname(formObjectAdmin.getSurname());
		res.setName(formObjectAdmin.getName());
		res.setId(formObjectAdmin.getId());
		res.setVATNumber(formObjectAdmin.getVATNumber());
		res.setCreditCard(card);
		res.setHasSpam(adminDB.getHasSpam());
		res.setMessages(adminDB.getMessages());
		res.setSocialProfiles(adminDB.getSocialProfiles());
		res.setUserAccount(adminDB.getUserAccount());
		res.setVersion(adminDB.getVersion());

		if (card.getNumber() != null)
			if (!this.creditCardService.validateNumberCreditCard(card))
				if (LocaleContextHolder.getLocale().getLanguage().toUpperCase().contains("ES"))
					binding.addError(new FieldError("formObject", "number", formObjectAdmin.getNumber(), false, null, null, "El numero de la tarjeta es invalido"));
				else
					binding.addError(new FieldError("formObject", "number", formObjectAdmin.getNumber(), false, null, null, "The card number is invalid"));

		if (card.getExpirationMonth() != null && card.getExpirationYear() != null)
			if (!this.creditCardService.validateDateCreditCard(card))
				if (LocaleContextHolder.getLocale().getLanguage().toUpperCase().contains("ES"))
					binding.addError(new FieldError("formObject", "expirationMonth", card.getExpirationMonth(), false, null, null, "La tarjeta no puede estar caducada"));
				else
					binding.addError(new FieldError("formObject", "expirationMonth", card.getExpirationMonth(), false, null, null, "The credit card can not be expired"));

		List<String> cardType = this.configurationService.getConfiguration().getCardType();

		if (!cardType.contains(res.getCreditCard().getBrandName()))
			if (LocaleContextHolder.getLocale().getLanguage().toUpperCase().contains("ES"))
				binding.addError(new FieldError("formObject", "brandName", card.getBrandName(), false, null, null, "Tarjeta no admitida"));
			else
				binding.addError(new FieldError("formObject", "brandName", card.getBrandName(), false, null, null, "The credit card is not accepted"));

		return res;

	}

	public Admin findOne(int id) {
		return this.adminRepository.findOne(id);
	}

	//----------------------------------------------BAN/UNBAN---------------------------------
	//----------------------------------------------------------------------------------------
	public void unBanSuspiciousActor(Actor a) {
		this.loggedAsAdmin();

		a.getUserAccount().setIsNotLocked(true);
		this.actorService.save(a);
	}

	public void banSuspiciousActor(Actor a) {
		this.loggedAsAdmin();

		Assert.isTrue(a.getHasSpam());

		a.getUserAccount().setIsNotLocked(false);
		this.actorService.save(a);
	}

	public void flush() {
		this.adminRepository.flush();

	}

}
