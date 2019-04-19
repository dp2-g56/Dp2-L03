
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
	@Autowired
	private HackerService hackerService;

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
	
	@RequestMapping(value = "/edit", method =RequestMethod .GET)
	public ModelAndView editPositionData(@RequestParam int positionDataId) {
		ModelAndView result;
		
		try {
			PositionData positionData = this.positionDataService.findOne(positionDataId);
			Curriculum curriculum = this.curriculumService.getCurriculumOfPositionData(positionDataId);
			Assert.notNull(this.curriculumService.getCurriculumOfLoggedHacker(curriculum.getId()));
			
			result = this.createEditModelAndView("hacker/editPositionData", positionData, curriculum.getId());
		} catch(Throwable oops) {
			result = new ModelAndView("redirect:/curriculum/hacker/list.do");
		}
		
		return result;
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod .GET)
	public ModelAndView deletePositionData(@RequestParam int positionDataId) {
		ModelAndView result = null;
		
		Curriculum curriculum = this.curriculumService.getCurriculumOfPositionData(positionDataId);
		
		try {
			this.positionDataService.deletePositionDataAsHacker(positionDataId);
			result = new ModelAndView("redirect:/curriculum/hacker/show.do?curriculumId=" + curriculum.getId());
		} catch(Throwable oops) {
			result = new ModelAndView("redirect:/curriculum/hacker/list.do");
		}
		
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
				result = new ModelAndView("redirect:/curriculum/hacker/show.do?curriculumId=" + curriculumId);
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
