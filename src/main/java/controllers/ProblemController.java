
package controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CompanyService;
import services.ProblemService;
import domain.Problem;

@Controller
@RequestMapping("/problem")
public class ProblemController extends AbstractController {

	@Autowired
	private ProblemService problemService;

	@Autowired
	private CompanyService companyService;

	public ProblemController() {
		super();
	}

	@RequestMapping(value = "/company/list", method = RequestMethod.GET)
	public ModelAndView listProblems() {
		ModelAndView result;

		this.companyService.loggedAsCompany();

		List<Problem> problems = new ArrayList<Problem>();

		problems = this.problemService.showProblems();

		result = new ModelAndView("problem/company/list");
		Boolean sameActorLogged = true;
		result.addObject("problems", problems);
		result.addObject("sameActorLogged", sameActorLogged);
		result.addObject("requestURI", "problem/company/list.do");

		return result;

	}

	@RequestMapping(value = "/company/listAttachments", method = RequestMethod.GET)
	public ModelAndView listAttachments(@RequestParam int problemId) {
		ModelAndView result;

		Problem problem = this.problemService.findOne(problemId);
		Boolean sameActorLogged = true;
		result = new ModelAndView("problem/company/listAttachments");
		result.addObject("attachments", problem.getAttachments());
		result.addObject("problemId", problemId);
		result.addObject("sameActorLogged", sameActorLogged);
		result.addObject("canEdit", problem.getIsDraftMode());

		return result;
	}

	@RequestMapping(value = "/company/addAttachment", method = RequestMethod.GET)
	public ModelAndView addAttachment(@RequestParam int problemId) {
		ModelAndView result;

		result = new ModelAndView("problem/company/addAttachment");
		result.addObject("problemId", problemId);

		return result;
	}

	@RequestMapping(value = "/company/create", method = RequestMethod.GET)
	public ModelAndView createProblem() {
		ModelAndView result;

		Problem problem = this.problemService.create();

		result = this.createEditModelAndView(problem);
		result.addObject("problem", problem);

		return result;
	}

	@RequestMapping(value = "/company/edit", method = RequestMethod.GET)
	public ModelAndView editProblem(@RequestParam int problemId) {
		ModelAndView result;

		Problem problem = this.problemService.findOne(problemId);
		List<Problem> problems = this.problemService.findAll();

		if (problems.contains(problem))
			result = this.createEditModelAndView(problem);
		else
			result = new ModelAndView("redirect:list.do");

		return result;
	}

	@RequestMapping(value = "/company/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Problem problem, BindingResult binding) {

		ModelAndView result;

		Problem p = this.problemService.create();

		p = this.problemService.reconstruct(problem, binding);

		if (binding.hasErrors()) {
			result = this.createEditModelAndView(problem);
		} else {
			try {
				this.companyService.addProblem(p);

				result = new ModelAndView("redirect:list.do");
			} catch (Throwable oops) {
				result = this.createEditModelAndView(problem, "company.commit.error");

			}
		}
		return result;
	}

	@RequestMapping(value = "/company/edit", method = RequestMethod.POST, params = "edit")
	public ModelAndView edit(Problem problem, BindingResult binding) {

		ModelAndView result;

		Problem p = this.problemService.create();

		p = this.problemService.reconstruct(problem, binding);

		if (binding.hasErrors()) {
			result = this.createEditModelAndView(problem);
		} else {
			try {
				this.problemService.updateProblem(p);

				result = new ModelAndView("redirect:list.do");
			} catch (Throwable oops) {
				result = this.createEditModelAndView(problem, "company.commit.error");

			}
		}
		return result;
	}

	@RequestMapping(value = "/company/addAttachment", method = RequestMethod.POST, params = "save")
	public ModelAndView addAttachment(@RequestParam int problemId, String attachment) {

		ModelAndView result;

		Problem problem = this.problemService.findOne(problemId);

		if (!this.problemService.isUrl(attachment.trim())) {
			result = new ModelAndView("problem/company/addAttachment");
			result.addObject("problemId", problemId);
			result.addObject("message", "company.notValidUrl");
		} else {

			try {
				this.problemService.addAttachment(attachment, problem);

				result = new ModelAndView("redirect:listAttachments.do");
				result.addObject("problemId", problemId);
			} catch (Throwable oops) {
				result = new ModelAndView("problem/company/addAttachment");
				result.addObject("problemId", problemId);
				result.addObject("message", "company.commit.error");
			}
		}

		return result;
	}

	@RequestMapping(value = "/company/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(Problem problem2) {

		ModelAndView result;

		Problem problem = this.problemService.findOne(problem2.getId());

		try {
			this.problemService.deleteProblem(problem);

			result = new ModelAndView("redirect:list.do");
		} catch (Throwable oops) {
			result = this.createEditModelAndView(problem, "company.commit.error");

		}
		return result;
	}

	@RequestMapping(value = "/company/deleteAttachment", method = RequestMethod.GET)
	public ModelAndView copy(@RequestParam int problemId, @RequestParam int attachmentNumber) {

		ModelAndView result;

		try {
			Problem problem = this.problemService.findOne(problemId);
			this.problemService.removeAttachment(problem, attachmentNumber);

		} catch (Throwable oops) {

		}

		result = new ModelAndView("redirect:listAttachments.do");
		result.addObject("problemId", problemId);
		return result;
	}


	protected ModelAndView createEditModelAndView(Problem problem) {
		ModelAndView result;

		result = this.createEditModelAndView(problem, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(Problem problem, String messageCode) {
		ModelAndView result;

		result = new ModelAndView("problem/company/edit");

		result.addObject("problem", problem);
		result.addObject("message", messageCode);

		return result;
	}
}
