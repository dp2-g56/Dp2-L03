
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
import services.PositionDataService;

@Controller
@RequestMapping("/positionData/hacker")
public class PositionDataHackerController extends AbstractController {
	
	@Autowired
	private CurriculumService curriculumService;
	@Autowired
	private PositionDataService positionDataService;

	public PositionDataHackerController() {
		super();
	}
	
	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public ModelAndView newPositionData(@RequestParam int curriculumId) {
		ModelAndView result;
		
		PositionData positionData = new PositionData();
		
		result = this.createEditModelAndView("hacker/createPositionData", positionData, curriculumId);
		
		return result;	
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST, params = "save")
	public ModelAndView savePositionData(@Valid PositionData positionData, BindingResult binding, @Valid int curriculumId) {
		ModelAndView result;
		
		String tiles;
		if(positionData.getId()==0) {
			tiles = "hacker/createPositionData";
		} else {
			tiles = "hacker/editPositionData";
		}
		
		if(binding.hasErrors()) {
			result = this.createEditModelAndView(tiles, positionData, curriculumId);
		} else {
			try {
				this.positionDataService.addOrUpdatePositionDataAsHacker(positionData, curriculumId);
				
				Curriculum curriculum = this.curriculumService.getCurriculumOfLoggedHacker(curriculumId);
				
				result = new ModelAndView("hacker/curriculum");
				result.addObject("curriculum", curriculum);
				result.addObject("personalData", curriculum.getPersonalData());
				result.addObject("positionData", curriculum.getPositionData());
				result.addObject("educationData", curriculum.getEducationData());
				result.addObject("miscellaneousData", curriculum.getMiscellaneousData());
				result.addObject("requestURI", "/curriculum/hacker/show.do");
			} catch(Throwable oops) {
				result = this.createEditModelAndView(tiles, positionData, curriculumId, "commit.error");
			}
		}
		
		return result;	
	}

	private ModelAndView createEditModelAndView(String tiles, PositionData positionData, int curriculumId) {
		ModelAndView result = new ModelAndView(tiles);
		result.addObject("positionData", positionData);
		result.addObject("curriculumId", curriculumId);
		return result;
	}
	
	private ModelAndView createEditModelAndView(String tiles, PositionData positionData, int curriculumId,
			String message) {
		ModelAndView result = this.createEditModelAndView(tiles, positionData, curriculumId);
		result.addObject("message", message);
		return result;
	}

}
