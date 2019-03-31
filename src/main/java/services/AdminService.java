
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

import repositories.AdminRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Admin;
import domain.CreditCard;
import domain.Message;
import domain.SocialProfile;
import forms.FormObjectAdmin;

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


	//----------------------------------------CRUD METHODS--------------------------
	//------------------------------------------------------------------------------

	public Admin save(Admin admin) {
		return this.adminRepository.save(admin);
	}

	//-----------------------------------------SECURITY-----------------------------
	//------------------------------------------------------------------------------

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

		//Confirmacion contrasena
		if (!formObjectAdmin.getPassword().equals(formObjectAdmin.getConfirmPassword())) {
			if (locale.contains("ES")) {
				binding.addError(new FieldError("formObjectAdmin", "password", formObjectAdmin.getPassword(), false, null, null, "Las contrasenas no coinciden"));
			} else {
				binding.addError(new FieldError("formObjectAdmin", "password", formObjectAdmin.getPassword(), false, null, null, "Passwords don't match"));
			}
		}

		//Confirmacion terminos y condiciones
		if (!formObjectAdmin.getTermsAndConditions()) {
			if (locale.contains("ES")) {
				binding.addError(new FieldError("formObjectAdmin", "termsAndConditions", formObjectAdmin.getTermsAndConditions(), false, null, null, "Debe aceptar los terminos y condiciones"));
			} else {
				binding.addError(new FieldError("formObjectAdmin", "termsAndConditions", formObjectAdmin.getTermsAndConditions(), false, null, null, "You must accept the terms and conditions"));
			}
		}

		if (card.getNumber() != null) {
			if (!this.creditCardService.validateNumberCreditCard(card)) {
				if (LocaleContextHolder.getLocale().getLanguage().toUpperCase().contains("ES")) {
					binding.addError(new FieldError("formObject", "number", formObjectAdmin.getNumber(), false, null, null, "El numero de la tarjeta es invalido"));
				} else {
					binding.addError(new FieldError("formObject", "number", formObjectAdmin.getNumber(), false, null, null, "The card number is invalid"));
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

		return result;
	}

	public void saveNewAdmin(Admin admin) {
		this.loggedAsAdmin();
		this.adminRepository.save(admin);
	}

	public List<Float> showStatisticsOfFinder() {

		this.finderService.updateAllFinders();

		List<Float> statistics = new ArrayList<Float>();

		statistics.add(this.adminRepository.minResultFinders());
		statistics.add(this.adminRepository.maxResultFinders());
		statistics.add(this.adminRepository.avgResultFinders());
		statistics.add(this.adminRepository.stddevResultFinders());

		if (this.adminRepository.ratioEmptyFinder() == null)
			statistics.add((float) 0);
		else
			statistics.add(this.adminRepository.ratioEmptyFinder());

		return statistics;

	}

	public List<Float> showStatisticsOfCurriculum() {

		List<Float> statistics = new ArrayList<Float>();

		statistics.add(this.adminRepository.minCurriculumPerHacker());
		statistics.add(this.adminRepository.maxCurriculumPerHacker());
		statistics.add(this.adminRepository.avgCurriculumPerHacker());
		statistics.add(this.adminRepository.stddevCurriculumPerHacker());

		return statistics;
	}
}
