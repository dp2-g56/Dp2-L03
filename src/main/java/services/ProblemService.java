
package services;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ProblemRepository;
import domain.Company;
import domain.Problem;
import forms.FormObjectPositionProblemCheckbox;

@Service
@Transactional
public class ProblemService {

	@Autowired
	private ProblemRepository	problemRepository;
	@Autowired
	private CompanyService		companyService;


	public List<Problem> getFinalProblemsByCompany(int companyId) {
		return this.problemRepository.getFinalProblemsByCompany(companyId);
	}

	public Problem findOne(int problemId) {
		return this.problemRepository.findOne(problemId);
	}

	//----------------------------CREATE/EDIT POSITION----------------------------------------------------------------------------
	//----------------------------------------------------------------------------------------------------------------------------
	public List<Problem> reconstructList(FormObjectPositionProblemCheckbox formObjectPositionProblemCheckbox) {

		List<Integer> ids = formObjectPositionProblemCheckbox.getProblems();

		Company loggedCompany = this.companyService.loggedCompany();

		List<Problem> problems = new ArrayList<>();

		for (Integer id : ids) {

			Problem problem = this.problemRepository.findOne(id);
			Assert.notNull(problem);
			Assert.isTrue(loggedCompany.getProblems().contains(problem));

			problems.add(problem);

		}
		return problems;
	}

}
