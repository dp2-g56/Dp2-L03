
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

import services.CompanyService;
import services.HackerService;
import services.PositionService;
import services.ProblemService;
import domain.Company;
import domain.Finder;
import domain.Hacker;
import domain.Position;
import domain.Problem;
import forms.FormObjectPositionProblemCheckbox;

@Controller
@RequestMapping("/finder/hacker")
public class FinderHackerController extends AbstractController {

	@Autowired
	private PositionService	positionService;
	@Autowired
	private HackerService	hackerService;


	public FinderHackerController() {
		super();
	}

	// -------------------------------------------------------------------
	// ---------------------------LIST------------------------------------

	// Listar Positions
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		
		Hacker hacker = this.hackerService.securityAndHacker();
		
		Finder finder = hacker.getFinder();
		
		return null;	
	}

}
