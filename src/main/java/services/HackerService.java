
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

import repositories.HackerRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Admin;
import domain.Application;
import domain.CreditCard;
import domain.Curriculum;
import domain.EducationData;
import domain.Finder;
import domain.Hacker;
import domain.MiscellaneousData;
import domain.PersonalData;
import domain.PositionData;
import domain.Problem;
import domain.Message;
import domain.SocialProfile;
import forms.FormObjectEditAdmin;
import forms.FormObjectEditHacker;
import forms.FormObjectHacker;

@Service
@Transactional
public class HackerService {

	@Autowired
	private HackerRepository		hackerRepository;
	@Autowired
	private CurriculumService curriculumService;
	@Autowired
	private ActorService actorService;
	@Autowired
	private PersonalDataService personalDataService;

	@Autowired
	private ApplicationService		applicationService;

	@Autowired
	private CreditCardService		creditCardService;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private FinderService			finderService;


	// Auxiliar methods
	public Hacker securityAndHacker() {
		UserAccount userAccount = LoginService.getPrincipal();
		String username = userAccount.getUsername();

		Hacker loggedHacker = this.hackerRepository.getHackerByUsername(username);
		Assert.notNull(loggedHacker);
		List<Authority> authorities = (List<Authority>) loggedHacker.getUserAccount().getAuthorities();
		Assert.isTrue(authorities.get(0).toString().equals("HACKER"));

		return loggedHacker;
	}
	
	public Boolean isHacker(Actor actor) {
		List<Authority> authorities = (List<Authority>) actor.getUserAccount().getAuthorities();
		return authorities.get(0).toString().equals("HACKER");
	}

	public Hacker loggedHacker() {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
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

	public void addOrUpdateCurriculum(Curriculum curriculum) {
		Hacker hacker = this.securityAndHacker();

		if(curriculum.getId() > 0) {
			Assert.notNull(this.curriculumService.getCurriculumOfHacker(hacker.getId(), curriculum.getId()));
			this.curriculumService.save(curriculum);
			this.curriculumService.flush();
		} else {			
			List<Curriculum> curriculums = hacker.getCurriculums();
			curriculums.add(curriculum);
			hacker.setCurriculums(curriculums);
			this.save(hacker);
			this.flush();
		}
	}

	public void flush() {
		this.hackerRepository.flush();
	}

	public Hacker createHacker() {
		Hacker hacker = new Hacker();
		CreditCard card = new CreditCard();
		Finder finder = new Finder();
		// Se crean las listas vacias
		// ACTOR
		List<SocialProfile> socialProfiles = new ArrayList<SocialProfile>();
		List<Message> messages = new ArrayList<Message>();

		UserAccount userAccount = new UserAccount();
		userAccount.setUsername("");
		userAccount.setPassword("");

		// Actor
		hacker.setAddress("");
		hacker.setEmail("");
		hacker.setCreditCard(card);
		hacker.setMessages(messages);
		hacker.setHasSpam(false);
		hacker.setName("");
		hacker.setPhone("");
		hacker.setPhoto("");
		hacker.setSocialProfiles(socialProfiles);
		hacker.setSurname("");
		hacker.setVATNumber("");
		hacker.setFinder(finder);

		List<Authority> authorities = new ArrayList<Authority>();

		Authority authority = new Authority();
		authority.setAuthority(Authority.HACKER);
		authorities.add(authority);

		userAccount.setAuthorities(authorities);
		userAccount.setIsNotLocked(true);

		hacker.setUserAccount(userAccount);

		return hacker;

	}

	public Hacker reconstruct(FormObjectHacker formObjectHacker, BindingResult binding) {

		Hacker result = this.createHacker();

		result.setAddress(formObjectHacker.getAddress());
		result.setEmail(formObjectHacker.getEmail());
		result.setHasSpam(false);
		result.setName(formObjectHacker.getName());
		result.setPhone(formObjectHacker.getPhone());
		result.setPhoto(formObjectHacker.getPhoto());
		result.setSurname(formObjectHacker.getSurname());
		result.setVATNumber(formObjectHacker.getVATNumber());

		CreditCard card = new CreditCard();

		card.setBrandName(formObjectHacker.getBrandName());
		card.setHolderName(formObjectHacker.getHolderName());
		card.setNumber(formObjectHacker.getNumber());
		card.setExpirationMonth(formObjectHacker.getExpirationMonth());
		card.setExpirationYear(formObjectHacker.getExpirationYear());
		card.setCvvCode(formObjectHacker.getCvvCode());

		result.setCreditCard(card);

		// USER ACCOUNT
		UserAccount userAccount = new UserAccount();

		// Authorities
		List<Authority> authorities = new ArrayList<Authority>();
		Authority authority = new Authority();
		authority.setAuthority(Authority.HACKER);
		authorities.add(authority);
		userAccount.setAuthorities(authorities);

		// locked
		userAccount.setIsNotLocked(true);

		// Username
		userAccount.setUsername(formObjectHacker.getUsername());

		// Password
		Md5PasswordEncoder encoder;
		encoder = new Md5PasswordEncoder();
		userAccount.setPassword(encoder.encodePassword(formObjectHacker.getPassword(), null));

		result.setUserAccount(userAccount);

		String locale = LocaleContextHolder.getLocale().getLanguage().toUpperCase();

		//Confirmacion contrasena
		if (!formObjectHacker.getPassword().equals(formObjectHacker.getConfirmPassword()))
			if (locale.contains("ES"))
				binding.addError(new FieldError("formObjectAdmin", "password", formObjectHacker.getPassword(), false, null, null, "Las contrasenas no coinciden"));
			else
				binding.addError(new FieldError("formObjectAdmin", "password", formObjectHacker.getPassword(), false, null, null, "Passwords don't match"));

		//Confirmacion terminos y condiciones
		if (!formObjectHacker.getTermsAndConditions())
			if (locale.contains("ES"))
				binding.addError(new FieldError("formObjectAdmin", "termsAndConditions", formObjectHacker.getTermsAndConditions(), false, null, null, "Debe aceptar los terminos y condiciones"));
			else
				binding.addError(new FieldError("formObjectAdmin", "termsAndConditions", formObjectHacker.getTermsAndConditions(), false, null, null, "You must accept the terms and conditions"));

		if (card.getNumber() != null)
			if (!this.creditCardService.validateNumberCreditCard(card))
				if (LocaleContextHolder.getLocale().getLanguage().toUpperCase().contains("ES"))
					binding.addError(new FieldError("formObject", "number", formObjectHacker.getNumber(), false, null, null, "El numero de la tarjeta es invalido"));
				else
					binding.addError(new FieldError("formObject", "number", formObjectHacker.getNumber(), false, null, null, "The card number is invalid"));

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

		if (result.getEmail().matches("[\\w.%-]+\\<[\\w.%-]+\\@+\\>|[\\w.%-]+"))
			if (LocaleContextHolder.getLocale().getLanguage().toUpperCase().contains("ES"))
				binding.addError(new FieldError("member", "email", result.getEmail(), false, null, null, "No sigue el patron ejemplo@dominio.asd o alias <ejemplo@dominio.asd>"));
			else
				binding.addError(new FieldError("member", "email", result.getEmail(), false, null, null, "Dont follow the pattern example@domain.asd or alias <example@domain.asd>"));

		return result;
	}

	public void deleteHacker() {
		Hacker hacker = new Hacker();

		this.loggedAsHacker();
		hacker = this.loggedHacker();

		Finder finder = hacker.getFinder();

		//Finder se borra solo, hay que quitar la lista de positions
		finder.getPositions().clear();
		//Curriculum se borra solo

		//Mensajes se borran solos

		//Socialprofile se borra solo

		this.applicationService.deleteAllApplication();

		this.hackerRepository.delete(hacker);

	}

	public Hacker findOne(int id) {
		return this.hackerRepository.findOne(id);
	}

	public String SocialProfilesToString() {
		String res = "";
		Actor actor = this.actorService.loggedActor();
		List<SocialProfile> socialProfiles = new ArrayList<SocialProfile>();
		StringBuilder sb = new StringBuilder();
		socialProfiles = actor.getSocialProfiles();

		Integer cont = 1;

		for (SocialProfile f : socialProfiles) {
			sb.append("Profile" + cont + " Name: " + f.getName() + " Nick: " + f.getNick() + " Profile link: " + f.getProfileLink()).append(System.getProperty("line.separator"));
			cont++;
		}
		return sb.toString();
	}
	
	public Hacker reconstructHackerPersonalData(FormObjectEditHacker formObjectHacker, BindingResult binding) {
		Hacker res = new Hacker();

		Hacker adminDB = this.findOne(formObjectHacker.getId());

		CreditCard card = new CreditCard();

		//Credit Card
		card.setBrandName(formObjectHacker.getBrandName());
		card.setCvvCode(formObjectHacker.getCvvCode());
		card.setExpirationMonth(formObjectHacker.getExpirationMonth());
		card.setExpirationYear(formObjectHacker.getExpirationYear());
		card.setHolderName(formObjectHacker.getHolderName());
		card.setNumber(formObjectHacker.getNumber());

		res.setAddress(formObjectHacker.getAddress());

		res.setEmail(formObjectHacker.getEmail());
		res.setPhone(formObjectHacker.getPhone());
		res.setPhoto(formObjectHacker.getPhoto());
		res.setSurname(formObjectHacker.getSurname());
		res.setName(formObjectHacker.getName());
		res.setId(formObjectHacker.getId());
		res.setVATNumber(formObjectHacker.getVATNumber());
		res.setCreditCard(card);
		res.setHasSpam(adminDB.getHasSpam());
		res.setMessages(adminDB.getMessages());
		res.setSocialProfiles(adminDB.getSocialProfiles());
		res.setUserAccount(adminDB.getUserAccount());
		res.setVersion(adminDB.getVersion());

		if (card.getNumber() != null)
			if (!this.creditCardService.validateNumberCreditCard(card))
				if (LocaleContextHolder.getLocale().getLanguage().toUpperCase().contains("ES"))
					binding.addError(new FieldError("formObject", "number", formObjectHacker.getNumber(), false, null, null, "El numero de la tarjeta es invalido"));
				else
					binding.addError(new FieldError("formObject", "number", formObjectHacker.getNumber(), false, null, null, "The card number is invalid"));

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
	
	public FormObjectEditHacker getFormObjectEditHacker(Hacker hacker) {

		FormObjectEditHacker res = new FormObjectEditHacker();

		//Company
		res.setAddress(hacker.getAddress());
		res.setName(hacker.getName());
		res.setVATNumber(hacker.getVATNumber());
		res.setPhoto(hacker.getPhoto());
		res.setEmail(hacker.getEmail());
		res.setAddress(hacker.getAddress());
		res.setSurname(hacker.getSurname());
		res.setPhone(hacker.getPhone());

		//Credit Card
		CreditCard c = hacker.getCreditCard();

		res.setHolderName(c.getHolderName());
		res.setBrandName(c.getBrandName());
		res.setNumber(c.getNumber());
		res.setExpirationMonth(c.getExpirationMonth());
		res.setExpirationYear(c.getExpirationYear());
		res.setCvvCode(c.getCvvCode());

		return res;

	}

}
