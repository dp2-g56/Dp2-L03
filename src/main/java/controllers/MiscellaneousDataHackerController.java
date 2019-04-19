
package controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import domain.Curriculum;
import domain.Hacker;
import domain.MiscellaneousData;
import domain.PositionData;
import services.CurriculumService;
import services.HackerService;
import services.MiscellaneousDataService;

@Controller
@RequestMapping("/miscellaneousData/hacker")
public class MiscellaneousDataHackerController extends AbstractController {
	
	@Autowired
	private CurriculumService curriculumService;
	@Autowired
	private MiscellaneousDataService miscellaneousDataService;
	@Autowired
	private HackerService hackerService;

	public MiscellaneousDataHackerController() {
		super();
	}

	@RequestMapping(value = "/listAttachments", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam int miscellaneousDataId) {
		ModelAndView result;
		
		try {
			List<String> attachments = this.miscellaneousDataService.getAttachmentsOfMiscellaneousDataOfLoggedHacker(miscellaneousDataId);
			
			result = new ModelAndView("hacker/attachments");
			result.addObject("attachments", attachments);
			result.addObject("miscellaneousDataId", miscellaneousDataId);
			result.addObject("curriculumId", this.curriculumService.getCurriculumOfMiscellaneousData(miscellaneousDataId).getId());
		} catch(Throwable oops) {
			result = new ModelAndView("redirect:/curriculum/hacker/list.do");
		}
		
		return result;	
	}
	
	@RequestMapping(value = "/newAttachment", method = RequestMethod.GET)
	public ModelAndView newAttachment(@RequestParam int miscellaneousDataId) {
		ModelAndView result;
		
		result = new ModelAndView("hacker/createAttachment");
		result.addObject("miscellaneousDataId", miscellaneousDataId);
		result.addObject("attachment", "");

		return result;	
	}
	
	@RequestMapping(value = "/deleteAttachment", method = RequestMethod.GET)
	public ModelAndView newAttachment(@RequestParam int miscellaneousDataId, @RequestParam int attachmentIndex) {
		ModelAndView result;
		
		try {
			this.miscellaneousDataService.deleteAttachmentAsHacker(miscellaneousDataId, attachmentIndex);
			result = new ModelAndView("redirect:listAttachments.do?miscellaneousDataId=" + miscellaneousDataId);
		} catch(Throwable oops) {
			result = new ModelAndView("redirect:/curriculum/hacker/list.do");
		}

		return result;	
	}
	
	@RequestMapping(value = "/saveAttachment", method = RequestMethod.POST, params = "save")
	public ModelAndView saveAttachment(@RequestParam int miscellaneousDataId, @RequestParam String attachment) {
		ModelAndView result;
		
		try {
			this.miscellaneousDataService.addAttachmentAsHacker(miscellaneousDataId, attachment);
			result = new ModelAndView("redirect:listAttachments.do?miscellaneousDataId=" + miscellaneousDataId);
		} catch(Throwable oops) {
			result = new ModelAndView("hacker/createAttachment");
			result.addObject("miscellaneousDataId", miscellaneousDataId);
			result.addObject("attachment", attachment);
			result.addObject("message", "commit.error");
		}
		
		return result;	
	}
	
	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public ModelAndView newMiscellaneousData(@RequestParam int curriculumId) {
		ModelAndView result;
		
		MiscellaneousData miscellaneousData = new MiscellaneousData();
		
		result = this.createEditModelAndView("hacker/createMiscellaneousData", miscellaneousData, curriculumId);
		
		return result;	
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod .GET)
	public ModelAndView editMiscellaneousData(@RequestParam int miscellaneousDataId) {
		ModelAndView result;
		
		try {
			MiscellaneousData miscellaneousData = this.miscellaneousDataService.getMiscellaneousDataOfLoggedHacker(miscellaneousDataId);
			Curriculum curriculum = this.curriculumService.getCurriculumOfMiscellaneousData(miscellaneousDataId);
			
			result = this.createEditModelAndView("hacker/editMiscellaneousData", miscellaneousData, curriculum.getId());
		} catch(Throwable oops) {
			result = new ModelAndView("redirect:/curriculum/hacker/list.do");
		}
		
		return result;
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod .GET)
	public ModelAndView deleteMiscellaneousData(@RequestParam int miscellaneousDataId) {
		ModelAndView result = null;
		
		Curriculum curriculum = this.curriculumService.getCurriculumOfMiscellaneousData(miscellaneousDataId);
		
		try {
			this.miscellaneousDataService.deleteMiscellaneousDataAsHacker(miscellaneousDataId);
			result = new ModelAndView("redirect:/curriculum/hacker/show.do?curriculumId=" + curriculum.getId());
		} catch(Throwable oops) {
			result = new ModelAndView("redirect:/curriculum/hacker/list.do");
		}
		
		return result;
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST, params = "save")
	public ModelAndView saveMiscellaneousData(@ModelAttribute("miscellaneousData") @Valid MiscellaneousData miscellaneousData, BindingResult binding, @Valid int curriculumId) {
		ModelAndView result;
		
		String tiles;
		if(miscellaneousData.getId()==0) {
			tiles = "hacker/createMiscellaneousData";
		} else {
			tiles = "hacker/editMiscellaneousData";
		}
		
		MiscellaneousData miscellaneousDataReconstructed = this.miscellaneousDataService.reconstruct(miscellaneousData, binding);
		
		if(binding.hasErrors()) {
			result = this.createEditModelAndView(tiles, miscellaneousDataReconstructed, curriculumId);
		} else {
			try {
				this.miscellaneousDataService.addOrUpdateMiscellaneousDataAsHacker(miscellaneousDataReconstructed, curriculumId);
				result = new ModelAndView("redirect:/curriculum/hacker/show.do?curriculumId=" + curriculumId);
			} catch(Throwable oops) {
				result = this.createEditModelAndView(tiles, miscellaneousDataReconstructed, curriculumId, "commit.error");
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
