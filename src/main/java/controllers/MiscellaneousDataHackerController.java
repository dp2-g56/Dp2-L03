
package controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import domain.Curriculum;
import domain.MiscellaneousData;
import domain.PositionData;
import services.CurriculumService;
import services.MiscellaneousDataService;

@Controller
@RequestMapping("/miscellaneousData/hacker")
public class MiscellaneousDataHackerController extends AbstractController {
	
	@Autowired
	private CurriculumService curriculumService;
	@Autowired
	private MiscellaneousDataService miscellaneousDataService;

	public MiscellaneousDataHackerController() {
		super();
	}

	@RequestMapping(value = "/listAttachments", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam int miscellaneousDataId) {
		ModelAndView result;
		
		List<String> attachments = this.miscellaneousDataService.getMiscellaneousDataOfLoggedHacker(miscellaneousDataId).getAttachments();
		
		result = new ModelAndView("hacker/attachments");
		result.addObject("attachments", attachments);
		
		return result;	
	}
	
	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public ModelAndView newMiscellaneousData(@RequestParam int curriculumId) {
		ModelAndView result;
		
		MiscellaneousData miscellaneousData = new MiscellaneousData();
		
		result = this.createEditModelAndView("hacker/createMiscellaneousData", miscellaneousData, curriculumId);
		
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
	
	@RequestMapping(value = "/save", method = RequestMethod.POST, params = "save")
	public ModelAndView saveMiscellaneousData(@Valid MiscellaneousData miscellaneousData, BindingResult binding, @Valid int curriculumId) {
		ModelAndView result;
		
		String tiles;
		if(miscellaneousData.getId()==0) {
			tiles = "hacker/createMiscellaneousData";
		} else {
			tiles = "hacker/editMiscellaneousData";
		}
		
		//RECONSTRUCT
		
		if(binding.hasErrors()) {
			result = this.createEditModelAndView(tiles, miscellaneousData, curriculumId);
		} else {
			try {
				this.miscellaneousDataService.addOrUpdateMiscellaneousDataAsHacker(miscellaneousData, curriculumId);
				
				Curriculum curriculum = this.curriculumService.getCurriculumOfLoggedHacker(curriculumId);
				
				result = new ModelAndView("hacker/curriculum");
				result.addObject("curriculum", curriculum);
				result.addObject("personalData", curriculum.getPersonalData());
				result.addObject("positionData", curriculum.getPositionData());
				result.addObject("educationData", curriculum.getEducationData());
				result.addObject("miscellaneousData", curriculum.getMiscellaneousData());
				result.addObject("requestURI", "/curriculum/hacker/show.do");
			} catch(Throwable oops) {
				result = this.createEditModelAndView(tiles, miscellaneousData, curriculumId, "commit.error");
			}
		}
		
		return result;	
	}

	private ModelAndView createEditModelAndView(String tiles, MiscellaneousData miscellaneousData, int curriculumId) {
		ModelAndView result = new ModelAndView(tiles);
		result.addObject("miscellaneousData", miscellaneousData);
		result.addObject("curriculumId", curriculumId);
		return result;
	}
	
	private ModelAndView createEditModelAndView(String tiles, MiscellaneousData miscellaneousData, int curriculumId,
			String message) {
		ModelAndView result = this.createEditModelAndView(tiles, miscellaneousData, curriculumId);
		result.addObject("message", message);
		return result;
	}
}
