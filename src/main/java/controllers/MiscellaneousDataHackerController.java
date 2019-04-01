
package controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import domain.Curriculum;
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
}
