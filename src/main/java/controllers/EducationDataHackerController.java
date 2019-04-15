
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
import domain.EducationData;
import domain.Hacker;
import domain.PersonalData;
import domain.PositionData;
import forms.FormObjectCurriculumPersonalData;
import services.CurriculumService;
import services.EducationDataService;
import services.HackerService;
import services.PersonalDataService;
import services.PositionDataService;

@Controller
@RequestMapping("/educationData/hacker")
public class EducationDataHackerController extends AbstractController {
	
	@Autowired
	private CurriculumService curriculumService;
	@Autowired
	private EducationDataService educationDataService;
	@Autowired
	private HackerService hackerService;

	public EducationDataHackerController() {
		super();
	}
	
	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public ModelAndView newEducationData(@RequestParam int curriculumId) {
		ModelAndView result;
		
		EducationData educationData = new EducationData();
		
		result = this.createEditModelAndView("hacker/createEducationData", educationData, curriculumId);
		
		return result;	
	}
	
//	@RequestMapping(value = "/edit", method =RequestMethod .GET)
//	public ModelAndView editPositionData(@RequestParam int positionDataId) {
//		ModelAndView result;
//		
//		PositionData positionData = this.positionDataService.findOne(positionDataId);
//		Curriculum curriculum = this.curriculumService.getCurriculumOfPositionData(positionDataId);
//		
//		result = this.createEditModelAndView("hacker/editPositionData", positionData, curriculum.getId());
//		
//		return result;
//	}
//	
//	@RequestMapping(value = "/delete", method =RequestMethod .GET)
//	public ModelAndView deletePositionData(@RequestParam int positionDataId) {
//		ModelAndView result = null;
//		
//		Curriculum curriculum = this.curriculumService.getCurriculumOfPositionData(positionDataId);
//		
//		try {
//			this.positionDataService.deletePositionDataAsHacker(positionDataId);
//			result = new ModelAndView("redirect:/curriculum/hacker/show.do?curriculumId=" + curriculum.getId());
//		} catch(Throwable oops) {
//			result = new ModelAndView("redirect:/curriculum/hacker/show.do?curriculumId=" + curriculum.getId());
//		}
//		
//		return result;
//	}
//	
//	@RequestMapping(value = "/save", method = RequestMethod.POST, params = "save")
//	public ModelAndView savePositionData(@Valid PositionData positionData, BindingResult binding, @Valid int curriculumId) {
//		ModelAndView result;
//		
//		String tiles;
//		if(positionData.getId()==0) {
//			tiles = "hacker/createPositionData";
//		} else {
//			tiles = "hacker/editPositionData";
//		}
//		
//		if(binding.hasErrors()) {
//			result = this.createEditModelAndView(tiles, positionData, curriculumId);
//		} else {
//			try {
//				this.positionDataService.addOrUpdatePositionDataAsHacker(positionData, curriculumId);
//				
//				Curriculum curriculum = this.curriculumService.getCurriculumOfLoggedHacker(curriculumId);
//				
//				result = new ModelAndView("hacker/curriculum");
//				result.addObject("curriculum", curriculum);
//				result.addObject("personalData", curriculum.getPersonalData());
//				result.addObject("positionData", curriculum.getPositionData());
//				result.addObject("educationData", curriculum.getEducationData());
//				result.addObject("miscellaneousData", curriculum.getMiscellaneousData());
//				result.addObject("requestURI", "/curriculum/hacker/show.do");
//			} catch(Throwable oops) {
//				result = this.createEditModelAndView(tiles, positionData, curriculumId, "commit.error");
//			}
//		}
//		
//		return result;	
//	}

	private ModelAndView createEditModelAndView(String tiles, EducationData educationData, int curriculumId) {
		ModelAndView result = new ModelAndView(tiles);
		result.addObject("educationData", educationData);
		result.addObject("curriculumId", curriculumId);
		return result;
	}
	
	private ModelAndView createEditModelAndView(String tiles, EducationData educationData, int curriculumId,
			String message) {
		ModelAndView result = this.createEditModelAndView(tiles, educationData, curriculumId);
		result.addObject("message", message);
		return result;
	}

}