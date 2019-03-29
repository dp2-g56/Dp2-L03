
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ProblemService;
import domain.Problem;

@Controller
@RequestMapping("/problem")
public class ProblemController extends AbstractController {

	@Autowired
	private ProblemService	problemService;


	public ProblemController() {
		super();
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

		result = this.createEditModelAndView(problem);

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
