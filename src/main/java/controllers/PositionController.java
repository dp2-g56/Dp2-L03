
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

import services.ApplicationService;
import services.CompanyService;
import services.PositionService;
import services.ProblemService;
import domain.Application;
import domain.Company;
import domain.Position;
import domain.Problem;
import domain.Status;
import forms.FormObjectPositionProblemCheckbox;

@Controller
@RequestMapping("/position/company")
public class PositionController extends AbstractController {

	@Autowired
	private CompanyService		companyService;
	@Autowired
	private PositionService		positionService;
	@Autowired
	private ProblemService		problemService;
	@Autowired
	private ApplicationService	applicationService;


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

	//--------------------------LISTA DE PROBLEMAS----------------------------
	//------------------------------------------------------------------------
	@RequestMapping(value = "/problem/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam int positionId) {
		ModelAndView result;

		Company loggedCompany = this.companyService.loggedCompany();

		List<Problem> allProblems = new ArrayList<>();

		Position position = this.positionService.findOne(positionId);

		if (position.getIsDraftMode())
			return this.list();

		allProblems = position.getProblems();

		result = new ModelAndView("problemPosition/company/list");

		result.addObject("allProblems", allProblems);
		result.addObject("requestURI", "problem/company/list.do");
		result.addObject("positionId", positionId);

		return result;
	}

	//--------------------------LISTA DE APPLICATIONS----------------------------
	//---------------------------------------------------------------------------
	@RequestMapping(value = "/application/list", method = RequestMethod.GET)
	public ModelAndView listApplication(@RequestParam int positionId) {
		ModelAndView result;

		Company loggedCompany = this.companyService.loggedCompany();

		List<Application> allApplications = new ArrayList<>();

		Position position = this.positionService.findOne(positionId);

		if (position.getIsDraftMode())
			return this.list();

		allApplications = this.applicationService.getApplicationsCompany(positionId);

		result = new ModelAndView("applicationPosition/company/list");

		result.addObject("allApplications", allApplications);
		result.addObject("requestURI", "application/company/list.do");
		result.addObject("positionId", positionId);

		return result;
	}

	//------------------------------EDIT APPLICATION STATUS----------------------------
	//---------------------------------------------------------------------------------
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

	// ACCEPT APPLICATION
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

	//--------------------------LISTA DE ATTACHEMENTS----------------------------
	//---------------------------------------------------------------------------
	@RequestMapping(value = "/problemPosition/company/attachement/list", method = RequestMethod.GET)
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

	//---------------------------REQUIRED TECH-----------------------------------
	//---------------------------------------------------------------------------
	//LIST
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

	//---------------------------REQUIRED SKILL-----------------------------
	//----------------------------------------------------------------------
	//LIST
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

		if (!position.getIsDraftMode() || position.getIsCancelled())
			return this.list();

		if (!(company.getPositions().contains(position)))
			return this.list();

		FormObjectPositionProblemCheckbox formObjectPositionProblemCheckbox = this.positionService.prepareFormObjectPositionProblemCheckbox(positionId);

		result = this.createEditModelAndView(formObjectPositionProblemCheckbox);

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

	//SAVE POSITION
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid FormObjectPositionProblemCheckbox formObjectPositionProblemCheckbox, BindingResult binding) {

		ModelAndView result;

		Position position = new Position();
		position = this.positionService.createPosition();

		if (binding.hasErrors())
			result = this.createEditModelAndView(position);
		else
			try {

				List<Problem> problems = this.problemService.reconstructList(formObjectPositionProblemCheckbox);

				position = this.positionService.reconstructCheckBox(formObjectPositionProblemCheckbox, binding);

				this.positionService.saveAssignList(position, problems);

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

	protected ModelAndView createEditModelAndView(FormObjectPositionProblemCheckbox formObjectPositionProblemCheckbox, String messageCode) {
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
	public ModelAndView delete(FormObjectPositionProblemCheckbox formObjectPositionProblemCheckbox, BindingResult binding) {

		ModelAndView result;

		try {
			this.positionService.deletePositionWithId(formObjectPositionProblemCheckbox.getId());
			result = new ModelAndView("redirect:/position/company/list.do");
		} catch (Throwable oops) {
			result = this.createEditModelAndView(formObjectPositionProblemCheckbox, "commit.error");
		}
		return result;
	}

	//	// MODEL AND VIEW APPLICATION
	//	protected ModelAndView createEditModelAndView(Application application) {
	//		ModelAndView result;
	//
	//		result = this.createEditModelAndView(application, null);
	//
	//		return result;
	//	}
	//
	//	protected ModelAndView createEditModelAndView(Application application, String messageCode) {
	//		ModelAndView result;
	//
	//		List<Status> listStatus = Arrays.asList(Status.values());
	//		result = new ModelAndView("CREAR MODEL AND VIEW CON NOMBRE GRACIOSO");
	//		result.addObject("application", application);
	//		result.addObject("message", messageCode);
	//		result.addObject("listStatus", listStatus);
	//		//		result.addObject("positionId", formObjectPositionProblemCheckbox.getId());
	//
	//		return result;
	//	}

}
