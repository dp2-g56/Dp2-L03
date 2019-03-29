
package services;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ProblemRepository;
import domain.Company;
import domain.Problem;

@Service
@Transactional
public class ProblemService {

	@Autowired
	private ProblemRepository	problemRepository;

	@Autowired
	private CompanyService		companyService;


	public List<Problem> findAll() {
		return this.problemRepository.findAll();
	}

	public Problem findOne(int id) {
		return this.problemRepository.findOne(id);
	}

	public Problem save(Problem problem) {
		return this.problemRepository.save(problem);
	}

	public void delete(Problem problem) {
		this.problemRepository.delete(problem);
	}

	public List<Problem> showProblems() {
		Company company = this.companyService.getLoggedCompany();
		List<Problem> problems = new ArrayList<Problem>();
		problems = this.problemRepository.getProblemsByCompany(company);
		return problems;
	}

	public Problem create() {
		Problem problem = new Problem();
		List<String> attachment = new ArrayList<String>();

		problem.setAttachments(attachment);
		problem.setTitle("");
		problem.setHint("");
		problem.setIsDraftMode(null);
		problem.setStatement("");

		return problem;
	}

	public Problem addAttachment(String attachment, Problem problem) {

		Assert.isTrue(!attachment.trim().isEmpty() && this.isUrl(attachment));

		problem.getAttachments().add(attachment);
		return this.save(problem);
	}

	public Boolean isUrl(String url) {
		try {
			new URL(url).toURI();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
