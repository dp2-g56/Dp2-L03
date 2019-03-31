
package controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.AdminService;

@Controller
@RequestMapping("/statistics/administrator")
public class AdministratorStatisticsController {

	@Autowired
	private AdminService	adminService;


	public AdministratorStatisticsController() {
		super();
	}

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView statistics() {
		ModelAndView result;

		String locale = LocaleContextHolder.getLocale().getLanguage().toUpperCase();

		List<Float> statisticsCurriculum = this.adminService.showStatisticsOfCurriculum();
		List<Float> statisticsFinder = this.adminService.showStatisticsOfFinder();

		result = new ModelAndView("statistics/administrator/show");

		result.addObject("statisticsCurriculum", statisticsCurriculum);
		result.addObject("statisticsFinder", statisticsFinder);
		result.addObject("locale", locale);

		return result;
	}
}
