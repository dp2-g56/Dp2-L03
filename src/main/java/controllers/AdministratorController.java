/*
 * AdministratorController.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.AdminService;
import services.ConfigurationService;
import domain.Actor;
import domain.Admin;
import domain.Configuration;
import forms.FormObjectAdmin;

@Controller
@RequestMapping("/administrator")
public class AdministratorController extends AbstractController {

	@Autowired
	private AdminService			adminService;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private ActorService			actorService;


	// Constructors -----------------------------------------------------------

	public AdministratorController() {
		super();
	}

	@RequestMapping(value = "/administrator/create", method = RequestMethod.GET)
	public ModelAndView createAdmin() {
		ModelAndView result;

		FormObjectAdmin formObjectAdmin = new FormObjectAdmin();
		formObjectAdmin.setTermsAndConditions(false);

		result = this.createEditModelAndView(formObjectAdmin);

		return result;
	}

	@RequestMapping(value = "/administrator/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid FormObjectAdmin formObjectAdmin, BindingResult binding) {

		ModelAndView result;

		Admin admin = new Admin();
		admin = this.adminService.createAdmin();

		Configuration configuration = this.configurationService.getConfiguration();
		String prefix = configuration.getSpainTelephoneCode();

		//Reconstruccion
		admin = this.adminService.reconstruct(formObjectAdmin, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(formObjectAdmin);
		else
			try {

				if (admin.getPhone().matches("([0-9]{4,})$"))
					admin.setPhone(prefix + admin.getPhone());
				this.adminService.saveNewAdmin(admin);

				result = new ModelAndView("redirect:/");

			} catch (Throwable oops) {
				result = this.createEditModelAndView(formObjectAdmin, "company.commit.error");

			}
		return result;
	}

	protected ModelAndView createEditModelAndView(FormObjectAdmin formObjectAdmin) {
		ModelAndView result;

		result = this.createEditModelAndView(formObjectAdmin, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(FormObjectAdmin formObjectAdmin, String messageCode) {
		ModelAndView result;

		String locale = LocaleContextHolder.getLocale().getLanguage().toUpperCase();
		List<String> cardType = this.configurationService.getConfiguration().getCardType();

		result = new ModelAndView("administrator/administrator/create");
		result.addObject("formObjectAdmin", formObjectAdmin);
		result.addObject("message", messageCode);
		result.addObject("locale", locale);
		result.addObject("cardType", cardType);

		return result;
	}

	//LIST SUSPICIOUS ACTORS
	@RequestMapping(value = "/suspicious/list", method = RequestMethod.GET)
	public ModelAndView suspicious() {
		ModelAndView result;
		List<Actor> actors = new ArrayList<Actor>();
		actors = this.actorService.findAll();

		result = new ModelAndView("suspicious/administrator/list");

		result.addObject("actors", actors);

		return result;

	}

	//BAN
	@RequestMapping(value = "/suspicious/ban", method = RequestMethod.GET)
	public ModelAndView ban(@RequestParam int actorId) {
		ModelAndView result;

		Actor actor;
		actor = this.actorService.findOne(actorId);

		this.adminService.banSuspiciousActor(actor);
		result = new ModelAndView("redirect:list.do");

		return result;

	}

	//UNBAN
	@RequestMapping(value = "/suspicious/unban", method = RequestMethod.GET)
	public ModelAndView unban(@RequestParam int actorId) {
		ModelAndView result;

		Actor actor;
		actor = this.actorService.findOne(actorId);

		this.adminService.unBanSuspiciousActor(actor);
		result = new ModelAndView("redirect:list.do");

		return result;

	}

}
