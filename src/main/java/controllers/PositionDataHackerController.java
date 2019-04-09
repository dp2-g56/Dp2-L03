
package controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import domain.Curriculum;
import domain.Hacker;
import domain.PersonalData;
import domain.PositionData;
import forms.FormObjectCurriculumPersonalData;
import services.CurriculumService;
import services.HackerService;
import services.PersonalDataService;

@Controller
@RequestMapping("/positionData/hacker")
public class PositionDataHackerController extends AbstractController {
	
	@Autowired
	private CurriculumService curriculumService;
	@Autowired
	private HackerService hackerService;

	public PositionDataHackerController() {
		super();
	}
	
	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public ModelAndView newPositionData(@RequestParam int curriculumId) {
		ModelAndView result;
		
		PositionData positionData = new PositionData();
		
		result = this.createEditModelAndView("hacker/createPositionData", positionData);
		
		return result;	
	}

	private ModelAndView createEditModelAndView(String tiles, PositionData positionData) {
		ModelAndView result = new ModelAndView(tiles);
		result.addObject("positionData", positionData);
		return result;
	}

}
