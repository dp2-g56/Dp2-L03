
package controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.AdminService;
import domain.Company;
import domain.Hacker;
import domain.Position;

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
		this.adminService.loggedAsAdmin();

		String locale = LocaleContextHolder.getLocale().getLanguage().toUpperCase();

		List<Float> statisticsCurriculum = this.adminService.showStatisticsOfCurriculum();
		List<Float> statisticsFinder = this.adminService.showStatisticsOfFinder();
		List<Float> statisticsPositionsCompany = this.adminService.showStatisticsOfPositionsPerCompany();
		List<Float> statisticsApplicationsHacker = this.adminService.showStatisticsOfApplicationsPerHacker();
		List<Float> statisticsSalaries = this.adminService.showStatisticsOfSalaries();

		List<Company> companiesMorePositions = this.adminService.companiesMorePositions();
		List<Hacker> hackersMoreApplications = this.adminService.hackersMoreApplications();
		List<Position> bestPositionsSalary = this.adminService.bestSalaryPositions();
		List<Position> worstPositionsSalary = this.adminService.worstSalaryPositions();

		result = new ModelAndView("statistics/administrator/show");

		result.addObject("statisticsPositionsCompany", statisticsPositionsCompany);
		result.addObject("statisticsApplicationsHacker", statisticsApplicationsHacker);
		result.addObject("statisticsSalaries", statisticsSalaries);
		result.addObject("companiesMorePositions", companiesMorePositions);
		result.addObject("hackersMoreApplications", hackersMoreApplications);
		result.addObject("bestPositionsSalary", bestPositionsSalary);
		result.addObject("worstPositionsSalary", worstPositionsSalary);
		result.addObject("statisticsCurriculum", statisticsCurriculum);
		result.addObject("statisticsFinder", statisticsFinder);
		result.addObject("locale", locale);

		return result;
	}
}
