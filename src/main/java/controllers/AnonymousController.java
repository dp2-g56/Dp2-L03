
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
import services.ApplicationService;
import services.CompanyService;
import services.ConfigurationService;
import services.HackerService;
import services.PositionService;
import services.ProblemService;
import domain.Actor;
import domain.Application;
import domain.Company;
import domain.Configuration;
import domain.Curriculum;
import domain.Hacker;
import domain.Position;
import domain.Problem;
import forms.FormObjectCompany;
import forms.FormObjectHacker;

@Controller
@RequestMapping("/anonymous")
public class AnonymousController extends AbstractController {

	@Autowired
	private HackerService			hackerService;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private CompanyService			companyService;

	@Autowired
	private PositionService			positionService;

	@Autowired
	private ApplicationService		applicationService;

	@Autowired
	private ProblemService			problemService;

	@Autowired
	private ActorService			actorService;


	public AnonymousController() {
		super();
	}

	@RequestMapping(value = "/termsAndConditionsEN", method = RequestMethod.GET)
	public ModelAndView listEN() {
		ModelAndView result;

		result = new ModelAndView("termsAndConditionsEN");

		return result;
	}

	@RequestMapping(value = "/termsAndConditionsES", method = RequestMethod.GET)
	public ModelAndView listES() {

		ModelAndView result;

		result = new ModelAndView("termsAndConditionsES");

		return result;
	}

	//CREATE HACKER-----------------------------------------------------------------------

	@RequestMapping(value = "/hacker/create", method = RequestMethod.GET)
	public ModelAndView createAdmin() {
		ModelAndView result;

		FormObjectHacker formObjectHacker = new FormObjectHacker();
		formObjectHacker.setTermsAndConditions(false);

		result = new ModelAndView("anonymous/hacker/create");

		result = this.createEditModelAndView(formObjectHacker);

		return result;
	}

	@RequestMapping(value = "/hacker/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid FormObjectHacker formObjectHacker, BindingResult binding) {

		ModelAndView result;

		Hacker hacker = new Hacker();
		hacker = this.hackerService.createHacker();

		Configuration configuration = this.configurationService.getConfiguration();
		String prefix = configuration.getSpainTelephoneCode();

		//Reconstruccion
		hacker = this.hackerService.reconstruct(formObjectHacker, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(formObjectHacker);
		else
			try {

				if (hacker.getPhone().matches("([0-9]{4,})$"))
					hacker.setPhone(prefix + hacker.getPhone());
				this.hackerService.save(hacker);

				result = new ModelAndView("redirect:/");

			} catch (Throwable oops) {
				result = this.createEditModelAndView(formObjectHacker, "company.commit.error");

			}
		return result;
	}

	protected ModelAndView createEditModelAndView(FormObjectHacker formObjectHacker) {
		ModelAndView result;

		result = this.createEditModelAndView(formObjectHacker, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(FormObjectHacker formObjectHacker, String messageCode) {
		ModelAndView result;

		String locale = LocaleContextHolder.getLocale().getLanguage().toUpperCase();
		List<String> cardType = this.configurationService.getConfiguration().getCardType();

		result = new ModelAndView("anonymous/hacker/create");
		result.addObject("formObjectHacker", formObjectHacker);
		result.addObject("message", messageCode);
		result.addObject("locale", locale);
		result.addObject("cardType", cardType);

		return result;
	}

	//END OF CREATE HACKER-----------------------------------------------------------------------

	//CREATE COMPANY-----------------------------------------------------------------------

	@RequestMapping(value = "/company/create", method = RequestMethod.GET)
	public ModelAndView createCompany() {
		ModelAndView result;

		FormObjectCompany formObjectCompany = new FormObjectCompany();
		formObjectCompany.setTermsAndConditions(false);

		result = new ModelAndView("anonymous/company/create");

		result = this.createEditModelAndView(formObjectCompany);

		return result;
	}

	@RequestMapping(value = "/company/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid FormObjectCompany formObjectCompany, BindingResult binding) {

		ModelAndView result;

		Company company = new Company();
		company = this.companyService.createCompany();

		Configuration configuration = this.configurationService.getConfiguration();
		String prefix = configuration.getSpainTelephoneCode();

		//Reconstruccion
		company = this.companyService.reconstruct(formObjectCompany, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(formObjectCompany);
		else
			try {

				if (company.getPhone().matches("([0-9]{4,})$"))
					company.setPhone(prefix + company.getPhone());
				this.companyService.save(company);

				result = new ModelAndView("redirect:/");

			} catch (Throwable oops) {
				result = this.createEditModelAndView(formObjectCompany, "company.commit.error");

			}
		return result;
	}

	protected ModelAndView createEditModelAndView(FormObjectCompany formObjectCompany) {
		ModelAndView result;

		result = this.createEditModelAndView(formObjectCompany, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(FormObjectCompany formObjectCompany, String messageCode) {
		ModelAndView result;

		String locale = LocaleContextHolder.getLocale().getLanguage().toUpperCase();
		List<String> cardType = this.configurationService.getConfiguration().getCardType();

		result = new ModelAndView("anonymous/company/create");
		result.addObject("formObjectCompany", formObjectCompany);
		result.addObject("message", messageCode);
		result.addObject("locale", locale);
		result.addObject("cardType", cardType);

		return result;
	}

	//END OF CREATE COMPANY-----------------------------------------------------------------------

	@RequestMapping(value = "/position/list", method = RequestMethod.GET)
	public ModelAndView listPositions() {

		ModelAndView result;

		List<Position> publicPositions = new ArrayList<Position>();

		publicPositions = this.companyService.AllPositionsInFinal();

		result = new ModelAndView("anonymous/position/list");
		result.addObject("publicPositions", publicPositions);
		result.addObject("requestURI", "anonymous/position/list.do");

		return result;
	}

	@RequestMapping(value = "/curriculum/list", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam int applicationId) {
		ModelAndView result;

		try {

			Application application = this.applicationService.findOne(applicationId);
			Curriculum curriculum = application.getCurriculum();

			Boolean publicData = true;

			result = new ModelAndView("anonymous/curriculum/list");
			result.addObject("curriculum", curriculum);
			result.addObject("publicData", publicData);
			result.addObject("personalData", curriculum.getPersonalData());
			result.addObject("positionData", curriculum.getPositionData());
			result.addObject("educationData", curriculum.getEducationData());
			result.addObject("miscellaneousData", curriculum.getMiscellaneousData());
			result.addObject("requestURI", "/anonymous/curriculum/list.do");
		} catch (Throwable oops) {
			result = new ModelAndView("redirect:list.do");
		}

		return result;
	}

	@RequestMapping(value = "/problem/list", method = RequestMethod.GET)
	public ModelAndView listProblems(@RequestParam int positionId) {
		ModelAndView result;

		List<Problem> allProblems = new ArrayList<>();
		allProblems = this.positionService.getProblemsOfPosition(positionId);
		Actor actor = this.positionService.getActorWithPosition(positionId);

		Actor loggedActor = this.actorService.loggedActor();
		Boolean sameActorLogged;
		Boolean publicData = true;

		if (loggedActor.equals(actor))
			sameActorLogged = true;
		else
			sameActorLogged = false;

		result = new ModelAndView("anonymous/problem/list");

		result.addObject("problems", allProblems);
		result.addObject("publicData", publicData);
		result.addObject("sameActorLogged", sameActorLogged);
		result.addObject("requestURI", "anonymous/problem/list.do");
		result.addObject("positionId", positionId);

		return result;
	}

	@RequestMapping(value = "/attachement/list", method = RequestMethod.GET)
	public ModelAndView listAttachement(@RequestParam int problemId) {

		ModelAndView result;

		List<String> list = new ArrayList<String>();

		Problem problem = this.problemService.findOne(problemId);

		list = problem.getAttachments();

		result = new ModelAndView("anonymous/attachement/list");

		result.addObject("attachments", list);
		result.addObject("requestURI", "anonymous/attachement/list.do");
		result.addObject("problemId", problemId);

		return result;
	}

	@RequestMapping(value = "/application/list", method = RequestMethod.GET)
	public ModelAndView listAplications(@RequestParam int positionId) {
		ModelAndView result;

		List<Application> allApplications = new ArrayList<Application>();
		allApplications = this.applicationService.getApplicationsCompany(positionId);
		Actor actor = this.positionService.getActorWithPosition(positionId);

		Actor loggedActor = this.actorService.loggedActor();
		Boolean sameActorLogged;

		if (loggedActor.equals(actor))
			sameActorLogged = true;
		else
			sameActorLogged = false;

		result = new ModelAndView("anonymous/application/list");

		result.addObject("allApplications", allApplications);
		result.addObject("sameActorLogged", sameActorLogged);
		result.addObject("requestURI", "anonymous/application/list.do");
		result.addObject("positionId", positionId);

		return result;
	}

	@RequestMapping(value = "/company/listOne", method = RequestMethod.GET)
	public ModelAndView listCompany(@RequestParam int positionId) {

		ModelAndView result;

		Company company = this.companyService.companyOfRespectivePosition(positionId);
		Actor actor = this.positionService.getActorWithPosition(positionId);

		Actor loggedActor = this.actorService.loggedActor();
		Boolean sameActorLogged;

		if (loggedActor.equals(actor))
			sameActorLogged = true;
		else
			sameActorLogged = false;

		Boolean publicValue = true;

		result = new ModelAndView("anonymous/company/listOne");
		result.addObject("actor", company);
		result.addObject("publicValue", publicValue);
		result.addObject("sameActorLogged", sameActorLogged);
		result.addObject("requestURI", "anonymous/company/listOne.do");

		return result;
	}

	//SIGUIENTE DESPLEGABLE

	@RequestMapping(value = "/company/list", method = RequestMethod.GET)
	public ModelAndView listCompanies() {

		ModelAndView result;

		List<Company> companies = this.companyService.allCompanies();

		result = new ModelAndView("anonymous/company/list");
		result.addObject("companies", companies);
		result.addObject("requestURI", "anonymous/company/list.do");

		return result;
	}

	@RequestMapping(value = "/company/positions", method = RequestMethod.GET)
	public ModelAndView listPositionsOfCompany(@RequestParam int idCompany) {

		ModelAndView result;

		List<Position> positions = this.companyService.positionsOfCompanyInFinalNotCancelled(idCompany);

		result = new ModelAndView("anonymous/company/positions");
		result.addObject("publicPositionsSize", positions.size());
		result.addObject("publicPositions", positions);
		result.addObject("requestURI", "anonymous/company/positions.do");

		return result;
	}

	@RequestMapping(value = "/filtered/create", method = RequestMethod.GET)
	public ModelAndView newWord() {
		ModelAndView result;

		List<Position> filteredPositions = new ArrayList<Position>();
		filteredPositions = this.companyService.AllPositionsInFinal();

		result = new ModelAndView("anonymous/filtered/positions");
		result.addObject("publicPositions", filteredPositions);
		result.addObject("requestURI", "anonymous/filtered/positions.do");

		return result;
	}

	@RequestMapping(value = "/filtered/positions", method = RequestMethod.POST, params = "save")
	public ModelAndView listPositionsOfCompany(String word) {

		ModelAndView result;
		List<Position> filteredPositions = new ArrayList<Position>();

		filteredPositions = this.positionService.positionsFiltered(word);

		result = new ModelAndView("anonymous/filtered/positions");
		result.addObject("publicPositions", filteredPositions);
		result.addObject("requestURI", "anonymous/filtered/positions.do");

		return result;
	}
}
