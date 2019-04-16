
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
import forms.FormObjectCurriculumPersonalData;
import services.CurriculumService;
import services.HackerService;
import services.PersonalDataService;

@Controller
@RequestMapping("/curriculum/hacker")
public class CurriculumHackerController extends AbstractController {
	
	@Autowired
	private CurriculumService curriculumService;
	@Autowired
	private HackerService hackerService;
	@Autowired
	private PersonalDataService personalDataService;

	public CurriculumHackerController() {
		super();
	} 

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		
		try {
			List<Curriculum> curriculums = this.curriculumService.getCurriculumsOfLoggedHacker();
			
			result = new ModelAndView("hacker/curriculums");
			result.addObject("curriculums", curriculums);
		} catch(Throwable oops) {
			result = new ModelAndView("redirect:/");
		}
		
		return result;	
	}
	
	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam int curriculumId) {
		ModelAndView result;
		
		try {
			Curriculum curriculum = this.curriculumService.getCurriculumOfLoggedHacker(curriculumId);
			
			result = new ModelAndView("hacker/curriculum");
			result.addObject("curriculum", curriculum);
			result.addObject("personalData", curriculum.getPersonalData());
			result.addObject("positionData", curriculum.getPositionData());
			result.addObject("educationData", curriculum.getEducationData());
			result.addObject("miscellaneousData", curriculum.getMiscellaneousData());
			result.addObject("requestURI", "/curriculum/hacker/show.do");
		} catch(Throwable oops) {
			result = new ModelAndView("redirect:list.do");
		}
		
		return result;	
	}
	
	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public ModelAndView newCurriculum() {
		ModelAndView result;
		
		FormObjectCurriculumPersonalData formObject = new FormObjectCurriculumPersonalData();
		
		result = this.createEditModelAndView("hacker/createCurriculum", formObject);
		
		return result;	
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView editCurriculum(@RequestParam int curriculumId) {
		ModelAndView result;
		
		try {
			FormObjectCurriculumPersonalData formObject = new FormObjectCurriculumPersonalData();
			
			Curriculum curriculum = this.curriculumService.getCurriculumOfLoggedHacker(curriculumId);
			
			formObject.setId(curriculum.getId());
			formObject.setTitle(curriculum.getTitle());
			formObject.setFullName(curriculum.getPersonalData().getFullName());
			formObject.setPhoneNumber(curriculum.getPersonalData().getPhoneNumber());
			formObject.setStatement(curriculum.getPersonalData().getStatement());
			formObject.setGitHubProfile(curriculum.getPersonalData().getGitHubProfile());
			formObject.setLinkedInProfile(curriculum.getPersonalData().getLinkedinProfile());
			
			result = this.createEditModelAndView("hacker/editCurriculum", formObject);
		} catch(Throwable oops) {
			result = new ModelAndView("redirect:list.do");
		}
		
		return result;	
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView deleteCurriculum(@RequestParam int curriculumId) {
		ModelAndView result;
		
		try {
			this.curriculumService.deleteCurriculumAsHacker(curriculumId);

			result = new ModelAndView("redirect:list.do");
		} catch(Throwable oops) {
			result = new ModelAndView("redirect:list.do");
		}
		
		return result;	
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST, params = "save")
	public ModelAndView saveCurriculum(@ModelAttribute("formObject") @Valid FormObjectCurriculumPersonalData formObject,
			BindingResult binding) {
		ModelAndView result;
		
		String tiles;
		if(formObject.getId()>0) {
			tiles = "hacker/editCurriculum";
		} else 
			tiles = "hacker/createCurriculum";

		PersonalData personalData = new PersonalData();
		Curriculum curriculum = new Curriculum();
		
		personalData = this.personalDataService.reconstruct(formObject, binding);
		curriculum = this.curriculumService.reconstruct(formObject, binding, personalData);
		
		if (binding.hasErrors()) {
			result = this.createEditModelAndView(tiles, formObject);
		} else {
			try {
				this.hackerService.addOrUpdateCurriculum(curriculum);
				result = new ModelAndView("redirect:list.do");
			} catch (Throwable oops) {
				result = this.createEditModelAndView(tiles, formObject, "commit.error");
			}
		}
		return result;
	}

	private ModelAndView createEditModelAndView(String tiles, FormObjectCurriculumPersonalData formObject,
			String message) {
		ModelAndView result;
		
		result = new ModelAndView(tiles);
		result.addObject("formObject", formObject);
		result.addObject("message", message);
		
		return result;
	}
	
	private ModelAndView createEditModelAndView(String tiles, FormObjectCurriculumPersonalData formObject) {
		return this.createEditModelAndView(tiles, formObject, null);
	}

}
