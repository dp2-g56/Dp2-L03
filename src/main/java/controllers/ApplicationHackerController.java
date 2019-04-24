package controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import domain.Application;
import domain.Curriculum;
import domain.Hacker;
import domain.Position;
import domain.Problem;
import domain.Status;
import services.ApplicationService;
import services.HackerService;
import services.MessageService;
import services.PositionService;

@Controller
@RequestMapping("/application/hacker")
public class ApplicationHackerController extends AbstractController {

	@Autowired
	private ApplicationService applicationService;

	@Autowired
	private HackerService hackerService;

	@Autowired
	private PositionService positionService;

	@Autowired
	private MessageService messageService;

	public ApplicationHackerController() {
		super();
	}

	// ----------------------------LIST------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;

		Hacker hacker = this.hackerService.loggedHacker();

		List<Application> applications = new ArrayList<Application>();

		applications = (List<Application>) this.applicationService.getApplicationsByHacker(hacker);

		result = new ModelAndView("application/hacker/list");

		result.addObject("applications", applications);
		result.addObject("requestURI", "application/hacker/list.do");

		return result;

	}

	@RequestMapping(value = "/filter", method = { RequestMethod.POST, RequestMethod.GET }, params = "refresh")
	public ModelAndView applicationsFilter(@RequestParam String fselect) {
		ModelAndView result;

		if (fselect.equals("ALL"))
			result = new ModelAndView("redirect:list.do");
		else {

			Status status = Status.ACCEPTED;
			if (fselect.equals("PENDING"))
				status = Status.PENDING;
			else if (fselect.equals("REJECTED"))
				status = Status.REJECTED;
			else if (fselect.equals("SUBMITTED"))
				status = Status.SUBMITTED;

			Hacker loggedHacker = this.hackerService.loggedHacker();
			List<Application> applications = (List<Application>) this.applicationService
					.getApplicationsByHackerAndStatus(loggedHacker, status);

			result = new ModelAndView("application/hacker/list");

			result.addObject("applications", applications);
			result.addObject("requestURI", "application/hacker/filter.do");
		}

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView createApplication() {
		ModelAndView result = null;
		Boolean res = false;

		Hacker hacker = this.hackerService.loggedHacker();

		if (hacker.getCurriculums().isEmpty() || this.positionService.getFinalPositions().isEmpty()) {
			result = this.list();
			res = true;
			result.addObject("res", res);
			return result;
		} else {

			List<Position> positions = this.positionService.getFinalPositionsAndNotCancelled();

			Application application = new Application();

			result = this.createEditModelAndView(application);
			result.addObject("application", application);
			result.addObject("curriculums", hacker.getCurriculums());
			result.addObject("positions", positions);

			return result;
		}
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView editApplication(@RequestParam int applicationId) {
		ModelAndView result;

		Hacker h = this.hackerService.loggedHacker();

		Application application = this.applicationService.findOne(applicationId);

		if (h.getApplications().contains(application) && application.getStatus().equals(Status.PENDING))
			result = this.createEditModelAndView(application);
		else
			result = new ModelAndView("redirect:list.do");

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("application") Application application, BindingResult binding) {

		ModelAndView result;

		Application p = new Application();

		Hacker hacker = this.hackerService.loggedHacker();

		List<Position> positions = this.positionService.findAll();

		List<Curriculum> curriculums = hacker.getCurriculums();

		p = this.applicationService.reconstruct(application, binding);

		if (binding.hasErrors()) {
			result = this.createEditModelAndView(p);
			result.addObject("curriculums", curriculums);
			result.addObject("positions", positions);
		} else {
			try {
				this.hackerService.addApplication(p);

				result = new ModelAndView("redirect:list.do");
				result.addObject("curriculums", curriculums);
				result.addObject("positions", positions);
			} catch (Throwable oops) {
				result = this.createEditModelAndView(application, "hacker.commit.error");
				result.addObject("curriculums", curriculums);
				result.addObject("positions", positions);
			}
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "edit")
	public ModelAndView edit(@ModelAttribute("application") Application application, BindingResult binding) {

		ModelAndView result;

		Application p = new Application();

		Hacker hacker = this.hackerService.loggedHacker();

		p = this.applicationService.reconstruct(application, binding);

		if (binding.hasErrors()) {
			result = this.createEditModelAndView(p);
			result.addObject("message", "hacker.url.error");
		} else {
			try {
				this.applicationService.save(p);
				this.messageService.notificationStatusApplicationSubmitted(p);
				result = new ModelAndView("redirect:list.do");
			} catch (Throwable oops) {
				result = this.createEditModelAndView(application, "hacker.commit.error");
			}
		}
		return result;
	}

	protected ModelAndView createEditModelAndView(Application application) {
		ModelAndView result;

		result = this.createEditModelAndView(application, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(Application application, String messageCode) {
		ModelAndView result;

		result = new ModelAndView("application/hacker/edit");

		result.addObject("application", application);
		result.addObject("message", messageCode);

		return result;
	}

}
