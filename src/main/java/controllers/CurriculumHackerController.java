
package controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import domain.Curriculum;
import services.CurriculumService;

@Controller
@RequestMapping("/curriculum/hacker")
public class CurriculumHackerController extends AbstractController {
	
	@Autowired
	private CurriculumService curriculumService;

	public CurriculumHackerController() {
		super();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		
		List<Curriculum> curriculums = this.curriculumService.getCurriculumsOfLoggedHacker();
		
		result = new ModelAndView("hacker/curriculums");
		result.addObject("curriculums", curriculums);
		
		return result;	
	}
}
