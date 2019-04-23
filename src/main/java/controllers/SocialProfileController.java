
package controllers;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import security.LoginService;
import security.UserAccount;
import services.ActorService;
import services.AdminService;
import services.CompanyService;
import services.ConfigurationService;
import services.HackerService;
import services.SocialProfileService;
import domain.Actor;
import domain.Admin;
import domain.Company;
import domain.Configuration;
import domain.Hacker;
import domain.SocialProfile;
import forms.FormObjectEditAdmin;
import forms.FormObjectEditCompany;
import forms.FormObjectEditHacker;

@Controller
@RequestMapping("/authenticated")
public class SocialProfileController extends AbstractController {

	@Autowired
	private ActorService actorService;

	@Autowired
	private SocialProfileService socialProfileService;

	@Autowired
	private CompanyService companyService;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private AdminService adminService;

	@Autowired
	private HackerService hackerService;

	// -------------------------------------------------------------------
	// ---------------------------LIST
	// BROTHERHOOD------------------------------------
	@RequestMapping(value = "/showProfile", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Actor logguedActor = new Actor();
		List<SocialProfile> socialProfiles = new ArrayList<SocialProfile>();

		result = new ModelAndView("authenticated/showProfile");

		logguedActor = this.actorService.getActorByUsername(userAccount.getUsername());
		socialProfiles = logguedActor.getSocialProfiles();
		Boolean trueValue = true;

		result.addObject("socialProfiles", socialProfiles);
		result.addObject("actor", logguedActor);
		result.addObject("trueValue", trueValue);
		result.addObject("requestURI", "authenticated/showProfile.do");

		return result;
	}

	// ---------------------------------------------------------------------
	@RequestMapping(value = "/socialProfile/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		SocialProfile socialProfile;

		socialProfile = this.socialProfileService.create();
		result = this.createEditModelAndView(socialProfile);

		return result;
	}

	// ------------------------------------------------------------------------------------
	// ---------------------------EDIT SOCIAL
	// PROFILE--------------------------------------
	@RequestMapping(value = "/socialProfile/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam int socialProfileId) {

		ModelAndView result;
		SocialProfile socialProfile;

		Actor logged = this.actorService.getActorByUsername(LoginService.getPrincipal().getUsername());

		List<SocialProfile> socialProfiles = logged.getSocialProfiles();

		socialProfile = this.socialProfileService.findOne(socialProfileId);
		Assert.notNull(socialProfile);
		result = this.createEditModelAndView(socialProfile);

		if (!(socialProfiles.contains(socialProfile)))
			result = this.list();
		return result;
	}

	// ---------------------------------------------------------------------
	// ---------------------------SAVE --------------------------------------
	@RequestMapping(value = "/socialProfile/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(SocialProfile socialProfile, BindingResult binding) {
		ModelAndView result;
		Actor logguedActor = this.actorService.getActorByUsername(LoginService.getPrincipal().getUsername());

		socialProfile = this.socialProfileService.reconstruct(socialProfile, binding);

		System.out.println(binding);
		if (binding.hasErrors())
			result = this.createEditModelAndView(socialProfile);
		else
			try {

				SocialProfile saved = this.socialProfileService.save(socialProfile);
				List<SocialProfile> socialProfiles = logguedActor.getSocialProfiles();

				if (socialProfiles.contains(socialProfile)) {
					socialProfiles.remove(saved);
					socialProfiles.add(saved);
				} else
					socialProfiles.add(saved);

				logguedActor.setSocialProfiles(socialProfiles);

				this.actorService.save(logguedActor);

				result = new ModelAndView("redirect:/authenticated/showProfile.do");
			} catch (Throwable oops) {
				result = this.createEditModelAndView(socialProfile, "socialProfile.commit.error");
			}
		return result;
	}

	// ---------------------------------------------------------------------
	// ---------------------------DELETE------------------------------------
	@RequestMapping(value = "/socialProfile/create", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(SocialProfile socialProfile, BindingResult binding) {

		ModelAndView result;

		socialProfile = this.socialProfileService.reconstruct(socialProfile, binding);

		try {

			this.socialProfileService.deleteSocialProfile(socialProfile);
			result = new ModelAndView("redirect:/authenticated/showProfile.do");

		} catch (Throwable oops) {
			result = this.createEditModelAndView(socialProfile, "socialProfile.commit.error");
		}
		return result;
	}
	// ---------------------------------------------------------------------
	// ---------------------------EDIT PERSONAL DAT-------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView editPersonalData() {

		ModelAndView result = null;

		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();

		List<Authority> authorities = (List<Authority>) userAccount.getAuthorities();

		if (authorities.get(0).toString().equals("COMPANY")) {
			Company company = this.companyService.loggedCompany();
			Assert.notNull(company);
			FormObjectEditCompany formCompany = this.companyService.getFormObjectEditCompany(company);
			formCompany.setId(company.getId());
			result = this.createEditModelAndView(formCompany);

		} else if (authorities.get(0).toString().equals("ADMIN")) {
			Admin admin = this.adminService.loggedAdmin();
			Assert.notNull(admin);
			FormObjectEditAdmin formAdmin = this.adminService.getFormObjectEditadmin(admin);
			formAdmin.setId(admin.getId());
			result = this.createEditModelAndView(formAdmin);
		} else if (authorities.get(0).toString().equals("HACKER")) {
			Hacker hacker = this.hackerService.loggedHacker();
			Assert.notNull(hacker);
			FormObjectEditHacker formHacker = this.hackerService.getFormObjectEditHacker(hacker);
			formHacker.setId(hacker.getId());
			result = this.createEditModelAndView(formHacker);
		}

		if (result == null)
			result = this.list();

		result.addObject("cardType", this.configurationService.getConfiguration().getCardType());

		return result;
	}

	// ---------------------------------------------------------------------
	// ---------------------------SAVE PERSONAL DATA------------------------

	// Company
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView saveCompany(@Valid FormObjectEditCompany companyForm, BindingResult binding) {
		ModelAndView result;

		Company company = this.companyService.reconstructCompanyPersonalData(companyForm, binding);
		Configuration configuration = this.configurationService.getConfiguration();

		String prefix = configuration.getSpainTelephoneCode();

		if (binding.hasErrors()) {
			result = this.createEditModelAndView(companyForm);
			result.addObject("cardType", this.configurationService.getConfiguration().getCardType());
		} else
			try {
				if (company.getPhone().matches("(\\+[0-9]{1,3})(\\([0-9]{1,3}\\))([0-9]{4,})$")
						|| company.getPhone().matches("(\\+[0-9]{1,3})([0-9]{4,})$"))
					this.companyService.updateCompany(company);
				else if (company.getPhone().matches("([0-9]{4,})$")) {
					company.setPhone(prefix + company.getPhone());
					this.companyService.updateCompany(company);
				} else
					this.companyService.updateCompany(company);
				result = new ModelAndView("redirect:/authenticated/showProfile.do");
			} catch (Throwable oops) {
				result = this.createEditModelAndView(companyForm, "socialProfile.commit.error");
				result.addObject("cardType", this.configurationService.getConfiguration().getCardType());
			}

		return result;
	}

	@RequestMapping(value = "/admin/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView saveCompany(@Valid FormObjectEditAdmin adminForm, BindingResult binding) {
		ModelAndView result;

		Admin admin = this.adminService.reconstructCompanyPersonalData(adminForm, binding);
		Configuration configuration = this.configurationService.getConfiguration();

		String prefix = configuration.getSpainTelephoneCode();

		if (binding.hasErrors()) {
			result = this.createEditModelAndView(adminForm);
			result.addObject("cardType", this.configurationService.getConfiguration().getCardType());
		} else
			try {
				if (admin.getPhone().matches("(\\+[0-9]{1,3})(\\([0-9]{1,3}\\))([0-9]{4,})$")
						|| admin.getPhone().matches("(\\+[0-9]{1,3})([0-9]{4,})$"))
					this.adminService.save(admin);
				else if (admin.getPhone().matches("([0-9]{4,})$")) {
					admin.setPhone(prefix + admin.getPhone());
					this.adminService.save(admin);
				} else
					this.adminService.save(admin);
				result = new ModelAndView("redirect:/authenticated/showProfile.do");
			} catch (Throwable oops) {
				result = this.createEditModelAndView(adminForm, "socialProfile.commit.error");
				result.addObject("cardType", this.configurationService.getConfiguration().getCardType());
			}

		return result;
	}
	
	@RequestMapping(value = "/hacker/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView saveHacker(@Valid FormObjectEditHacker hackerForm, BindingResult binding) {
		ModelAndView result;

		Hacker hacker = this.hackerService.reconstructHackerPersonalData(hackerForm, binding);
		Configuration configuration = this.configurationService.getConfiguration();

		String prefix = configuration.getSpainTelephoneCode();

		if (binding.hasErrors()) {
			result = this.createEditModelAndView(hackerForm);
			result.addObject("cardType", this.configurationService.getConfiguration().getCardType());
		} else
			try {
				if (hacker.getPhone().matches("(\\+[0-9]{1,3})(\\([0-9]{1,3}\\))([0-9]{4,})$")
						|| hacker.getPhone().matches("(\\+[0-9]{1,3})([0-9]{4,})$"))
					this.hackerService.save(hacker);
				else if (hacker.getPhone().matches("([0-9]{4,})$")) {
					hacker.setPhone(prefix + hacker.getPhone());
					this.hackerService.save(hacker);
				} else
					this.hackerService.save(hacker);
				result = new ModelAndView("redirect:/authenticated/showProfile.do");
			} catch (Throwable oops) {
				result = this.createEditModelAndView(hackerForm, "socialProfile.commit.error");
				result.addObject("cardType", this.configurationService.getConfiguration().getCardType());
			}

		return result;
	}

	// ---------------------------------------------------------------------
	// ---------------------------CREATEEDITMODELANDVIEW--------------------

	protected ModelAndView createEditModelAndView(SocialProfile socialProfile) {

		ModelAndView result;

		result = this.createEditModelAndView(socialProfile, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(SocialProfile socialProfile, String messageCode) {

		ModelAndView result;

		result = new ModelAndView("authenticated/socialProfile/create");
		result.addObject("socialProfile", socialProfile);
		result.addObject("message", messageCode);

		return result;
	}

	// ---------------------------------------------------------------------
	// -------------------CREATEEDITMODELANDVIEW ACTOR----------------------

	// Company
	protected ModelAndView createEditModelAndView(FormObjectEditCompany company) {

		ModelAndView result;

		result = this.createEditModelAndView(company, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(FormObjectEditCompany company, String messageCode) {

		ModelAndView result;

		result = new ModelAndView("authenticated/edit");
		result.addObject("formObjectEditCompany", company);
		result.addObject("message", messageCode);

		return result;
	}

	protected ModelAndView createEditModelAndView(FormObjectEditAdmin admin) {

		ModelAndView result;

		result = this.createEditModelAndView(admin, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(FormObjectEditAdmin admin, String messageCode) {

		ModelAndView result;

		result = new ModelAndView("authenticated/edit");
		result.addObject("formObjectEditAdmin", admin);
		result.addObject("message", messageCode);

		return result;
	}
	
	protected ModelAndView createEditModelAndView(FormObjectEditHacker hacker) {

		ModelAndView result;

		result = this.createEditModelAndView(hacker, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(FormObjectEditHacker hacker, String messageCode) {

		ModelAndView result;

		result = new ModelAndView("authenticated/edit");
		result.addObject("formObjectEditHacker", hacker);
		result.addObject("message", messageCode);

		return result;
	}

}
