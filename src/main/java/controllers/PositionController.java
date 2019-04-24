
package controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ApplicationService;
import services.CompanyService;
import services.CurriculumService;

import services.FinderService;

import services.MessageService;

import services.PositionService;
import services.ProblemService;
import domain.Actor;
import domain.Application;
import domain.Company;
import domain.Curriculum;
import domain.Position;
import domain.Problem;
import domain.Status;
import forms.FormObjectPositionProblemCheckbox;

@Controller
@RequestMapping("/position/company")
public class PositionController extends AbstractController {

	@Autowired
	private CompanyService companyService;

	@Autowired
	private PositionService positionService;

	@Autowired
	private ProblemService problemService;

	@Autowired
	private ApplicationService applicationService;


	@Autowired
	private ActorService		actorService;

	@Autowired
	private FinderService		finderService;

	@Autowired
	private CurriculumService	curriculumService;


	public PositionController() {
		super();
	}

	// -------------------------------------------------------------------
	// ---------------------------LIST------------------------------------

	// Listar Positions
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {

		ModelAndView result;
		List<Position> positions;

		Company loggedCompany = this.companyService.loggedCompany();

		positions = loggedCompany.getPositions();

		result = new ModelAndView("position/company/list");
		result.addObject("positions", positions);
		result.addObject("requestURI", "position/company/list.do");

		return result;
	}

	// --------------------------LISTA DE PROBLEMAS----------------------------
	// ------------------------------------------------------------------------
	@RequestMapping(value = "/problem/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam int positionId) {
		ModelAndView result;

		Company loggedCompany = this.companyService.loggedCompany();

		List<Problem> allProblems = new ArrayList<>();

		Position position = this.positionService.findOne(positionId);

		allProblems = position.getProblems();

		result = new ModelAndView("problemPosition/company/list");

		result.addObject("allProblems", allProblems);
		result.addObject("requestURI", "problem/company/list.do");
		result.addObject("positionId", positionId);

		return result;
	}

	// --------------------------LISTA DE APPLICATIONS----------------------------
	// ---------------------------------------------------------------------------
	@RequestMapping(value = "/application/list", method = RequestMethod.GET)
	public ModelAndView listApplication(@RequestParam int positionId) {
		ModelAndView result;

		Company loggedCompany = this.companyService.loggedCompany();

		List<Application> allApplications = new ArrayList<>();

		Position position = this.positionService.findOne(positionId);

		if (position.getIsDraftMode())
			return this.list();

		allApplications = this.applicationService.getApplicationsCompany(positionId);
		Actor actor = this.positionService.getActorWithPosition(position.getId());

		Actor loggedActor = this.actorService.loggedActor();
		Boolean sameActorLogged;

		if (loggedActor.equals(actor))
			sameActorLogged = true;
		else
			sameActorLogged = false;

		result = new ModelAndView("applicationPosition/company/list");

		result.addObject("allApplications", allApplications);
		result.addObject("sameActorLogged", sameActorLogged);
		result.addObject("requestURI", "application/company/list.do");
		result.addObject("positionId", positionId);

		return result;
	}

	// ------------------------------SHOW
	// CURRICULUM------------------------------------
	// ---------------------------------------------------------------------------------
	@RequestMapping(value = "/curriculum/list", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam int applicationId) {
		ModelAndView result;

		try {

			Company loggedCompany = this.companyService.loggedCompany();
			Application application = this.applicationService.findOne(applicationId);
			Position position = application.getPosition();
			if (!loggedCompany.getPositions().contains(position))
				return this.list();

			Application application2 = this.applicationService.findOneWithAssert(applicationId);
			Curriculum curriculum = application2.getCurriculum();

			result = new ModelAndView("company/curriculum");
			result.addObject("curriculum", curriculum);
			result.addObject("personalData", curriculum.getPersonalData());
			result.addObject("positionData", curriculum.getPositionData());
			result.addObject("educationData", curriculum.getEducationData());
			result.addObject("miscellaneousData", curriculum.getMiscellaneousData());
			result.addObject("requestURI", "/curriculum/hacker/show.do");
		} catch (Throwable oops) {
			result = new ModelAndView("redirect:list.do");
		}

		return result;
	}

	// ------------------------------EDIT APPLICATION
	// STATUS----------------------------
	// ---------------------------------------------------------------------------------
	// ACCEPT APPLICATION
	@RequestMapping(value = "/application/accept", method = RequestMethod.GET)
	public ModelAndView acceptApplication(@RequestParam int applicationId) {
		ModelAndView result;
		Application application;
		application = this.applicationService.findOne(applicationId);
		Position position = application.getPosition();
		Company company = this.companyService.loggedCompany();
		List<Position> positions = company.getPositions();

		if (application.getStatus() != Status.SUBMITTED)
			return this.list();

		if (!(company.getPositions().contains(position)))
			return this.list();

		this.applicationService.editApplicationCompany(application, true);

		return this.list();
	}

	// REJECT APPLICATION
	@RequestMapping(value = "/application/reject", method = RequestMethod.GET)
	public ModelAndView rejectApplication(@RequestParam int applicationId) {
		ModelAndView result;
		Application application;
		application = this.applicationService.findOne(applicationId);
		Position position = application.getPosition();
		Company company = this.companyService.loggedCompany();
		List<Position> positions = company.getPositions();

		if (application.getStatus() != Status.SUBMITTED)
			return this.list();

		if (!(company.getPositions().contains(position)))
			return this.list();

		this.applicationService.editApplicationCompany(application, false);

		return this.list();
	}

	// --------------------------LISTA DE ATTACHEMENTS----------------------------
	// ---------------------------------------------------------------------------
	@RequestMapping(value = "/attachement/list", method = RequestMethod.GET)
	public ModelAndView listAttachement(@RequestParam int problemId) {

		ModelAndView result;

		List<String> list;

		Company loggedCompany = this.companyService.loggedCompany();

		Problem problem = this.problemService.findOne(problemId);

		if (!loggedCompany.getProblems().contains(problem))
			return this.list();

		list = problem.getAttachments();

		result = new ModelAndView("problemPosition/company/attachement/list");

		result.addObject("list", list);
		result.addObject("requestURI", "problemPosition/company/attachement/list.do");
		result.addObject("problemId", problemId);

		return result;
	}

	// ---------------------------REQUIRED TECH-----------------------------------
	// ---------------------------------------------------------------------------
	// LIST
	@RequestMapping(value = "/technology/list", method = RequestMethod.GET)
	public ModelAndView listTech(@RequestParam int positionId) {

		ModelAndView result;

		List<String> list;

		Company loggedCompany = this.companyService.loggedCompany();

		Position position = this.positionService.findOne(positionId);

		if (!loggedCompany.getPositions().contains(position))
			return this.list();

		list = position.getRequiredTecnologies();

		result = new ModelAndView("position/company/technology/list");

		result.addObject("list", list);
		result.addObject("requestURI", "position/company/technology/list.do");
		result.addObject("positionId", positionId);

		return result;
	}

	// ---------------------------REQUIRED SKILL-----------------------------
	// ----------------------------------------------------------------------
	// LIST
	@RequestMapping(value = "/skill/list", method = RequestMethod.GET)
	public ModelAndView listSkill(@RequestParam int positionId) {

		ModelAndView result;

		List<String> list;

		Company loggedCompany = this.companyService.loggedCompany();

		Position position = this.positionService.findOne(positionId);

		if (!loggedCompany.getPositions().contains(position))
			return this.list();

		list = position.getRequiredSkills();

		result = new ModelAndView("position/company/skill/list");

		result.addObject("list", list);
		result.addObject("requestURI", "position/company/skill/list.do");
		result.addObject("positionId", positionId);

		return result;
	}

	// CREATE POSITION
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView createPosition() {
		ModelAndView result;
		FormObjectPositionProblemCheckbox formObjectPositionProblemCheckbox = new FormObjectPositionProblemCheckbox();

		List<Integer> problems = new ArrayList<>();
		formObjectPositionProblemCheckbox.setProblems(problems);

		result = this.createEditModelAndView(formObjectPositionProblemCheckbox);

		return result;
	}

	// EDIT POSITION
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam int positionId) {
		ModelAndView result;
		Position position;
		position = this.positionService.findOne(positionId);
		Company company = this.companyService.loggedCompany();

		if (company.getPositions().contains(position)) {

			if (!position.getIsDraftMode() || position.getIsCancelled())
				return this.list();

			if (!(company.getPositions().contains(position)))
				return this.list();

			FormObjectPositionProblemCheckbox formObjectPositionProblemCheckbox = this.positionService
					.prepareFormObjectPositionProblemCheckbox(positionId);

			result = this.createEditModelAndView(formObjectPositionProblemCheckbox);

		} else
			result = new ModelAndView("redirect:list.do");

		return result;
	}

	// CANCEL POSITION
	@RequestMapping(value = "/cancel", method = RequestMethod.GET)
	public ModelAndView cancel(@RequestParam int positionId) {
		ModelAndView result;
		Position position;
		position = this.positionService.findOne(positionId);
		Company company = this.companyService.loggedCompany();

		if (position.getIsDraftMode() || position.getIsCancelled())
			return this.list();

		if (!(company.getPositions().contains(position)))
			return this.list();

		this.positionService.cancelPosition(position);

		return this.list();
	}

	// SAVE POSITION
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid FormObjectPositionProblemCheckbox formObjectPositionProblemCheckbox,
			BindingResult binding) {

		ModelAndView result;

		Position position = new Position();
		position = this.positionService.createPosition();
		List<Problem> problems = new ArrayList<>();
		Position positionSaved = new Position();

		problems = this.problemService.reconstructList(formObjectPositionProblemCheckbox);
		position = this.positionService.reconstructCheckBox(formObjectPositionProblemCheckbox, binding);
		Boolean errorProblems = false;

		if (formObjectPositionProblemCheckbox.getIsDraftMode() != null)
			if (!formObjectPositionProblemCheckbox.getIsDraftMode())
				errorProblems = !(problems.size() >= 2);

		if (binding.hasErrors() || errorProblems) {
			result = this.createEditModelAndView(position);
			if (errorProblems)
				result.addObject("message", "position.problemsError");
		} else
			try {
				positionSaved = this.positionService.saveAssignList(position, problems);
				if (positionSaved.getIsDraftMode() == false)
					this.finderService.sendNotificationPosition(position);
				result = new ModelAndView("redirect:/position/company/list.do");

			} catch (Throwable oops) {

				result = this.createEditModelAndView(position, "commit.error");

			}
		return result;
	}

	// MODEL AND VIEW POSITION CHECKBOX
	protected ModelAndView createEditModelAndView(FormObjectPositionProblemCheckbox formObjectPositionProblemCheckbox) {
		ModelAndView result;

		result = this.createEditModelAndView(formObjectPositionProblemCheckbox, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(FormObjectPositionProblemCheckbox formObjectPositionProblemCheckbox,
			String messageCode) {
		ModelAndView result;

		Map<Integer, String> map = new HashMap<>();

		map = this.positionService.getMapAvailableProblems();

		result = new ModelAndView("position/company/create");
		result.addObject("formObjectPositionProblemCheckbox", formObjectPositionProblemCheckbox);
		result.addObject("message", messageCode);
		result.addObject("map", map);
		result.addObject("positionId", formObjectPositionProblemCheckbox.getId());

		return result;
	}

	// MODEL AND VIEW POSITION
	protected ModelAndView createEditModelAndView(Position position) {
		ModelAndView result;

		result = this.createEditModelAndView(position, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(Position position, String messageCode) {
		ModelAndView result;

		Map<Integer, String> map = new HashMap<>();

		map = this.positionService.getMapAvailableProblems();

		result = new ModelAndView("position/company/create");
		result.addObject("position", position);
		result.addObject("message", messageCode);
		result.addObject("map", map);
		result.addObject("positionId", position.getId());

		return result;
	}

	// -------------------------------------------------------------------
	// ---------------------------DELETE----------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(FormObjectPositionProblemCheckbox formObjectPositionProblemCheckbox,
			BindingResult binding) {

		ModelAndView result;

		try {
			this.positionService.deletePositionWithId(formObjectPositionProblemCheckbox.getId());
			result = new ModelAndView("redirect:/position/company/list.do");
		} catch (Throwable oops) {
			result = this.createEditModelAndView(formObjectPositionProblemCheckbox, "commit.error");
		}
		return result;
	}

}
