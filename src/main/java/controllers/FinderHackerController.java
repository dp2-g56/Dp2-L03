
package controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CompanyService;
import services.FinderService;
import services.HackerService;
import services.PositionService;
import services.ProblemService;
import domain.Company;
import domain.Finder;
import domain.Hacker;
import domain.Position;
import domain.Problem;
import forms.FormObjectPositionProblemCheckbox;
import security.LoginService;
import security.UserAccount;

@Controller
@RequestMapping("/finder/hacker")
public class FinderHackerController extends AbstractController {

	@Autowired
	private PositionService	positionService;
	@Autowired
	private HackerService	hackerService;
	@Autowired
	private FinderService	finderService;


	public FinderHackerController() {
		super();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		
		try {
			Hacker hacker = this.hackerService.securityAndHacker();
			List<Position> positions = this.finderService.finderList(hacker.getFinder());
			
			result = new ModelAndView("hacker/finderResult");
			result.addObject("positions", positions);
			result.addObject("hacker", hacker);
		} catch(Throwable oops) {
			result = new ModelAndView("redirect:/");
		}
		
		return result;	
	}
	
	@RequestMapping(value = "/clean", method = RequestMethod.POST, params = "save")
	public ModelAndView save() {
		ModelAndView result;

		try {
			Hacker hacker = this.hackerService.securityAndHacker();
			this.finderService.getFinalPositionsAndCleanFinder(hacker.getFinder());

			result = new ModelAndView("redirect:list.do");
		} catch(Throwable oops) {
			result = new ModelAndView("redirect:list.do");
		}

		return result;

	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		
		try {
			Hacker hacker = this.hackerService.securityAndHacker();

			Finder finder = hacker.getFinder();
			Assert.notNull(finder);

			result = this.createEditModelAndView(finder);
		} catch(Throwable oops) {
			result = new ModelAndView("redirect:list.do");
		}

		return result;

	}
	
	// Save
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Finder finderForm, BindingResult binding) {
		ModelAndView result;

		Finder finder = this.finderService.reconstruct(finderForm, binding);
		
		if (binding.hasErrors())
			result = this.createEditModelAndView(finderForm);
		else
			try {
				this.finderService.filterPositionsByFinder(finder);
				result = new ModelAndView("redirect:list.do");
			} catch (Throwable oops) {
				try {
					Hacker hacker = this.hackerService.securityAndHacker();
					result = this.createEditModelAndView(finder, "finder.commit.error");
					result.addObject("hacker", hacker);
				} catch(Throwable oops2) {
					result = new ModelAndView("redirect:/");
				}
			}
		return result;
	}
	
	// CreateEditModelAndView
	protected ModelAndView createEditModelAndView(Finder finder) {
		ModelAndView result;

		result = this.createEditModelAndView(finder, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(Finder finder, String messageCode) {
		ModelAndView result;

		result = new ModelAndView("hacker/finder");

		result.addObject("finder", finder);
		result.addObject("message", messageCode);

		return result;

	}

}
