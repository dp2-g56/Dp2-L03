
package services;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;


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

	@Autowired
	private Validator			validator;


	//-------------------------------------------CRUD---------------------------------------------------
	//--------------------------------------------------------------------------------------------------

	public List<Problem> getFinalProblemsByCompany(int companyId) {
		return this.problemRepository.getFinalProblemsByCompany(companyId);
	}

	public Problem findOne(int problemId) {
		return this.problemRepository.findOne(problemId);
	}

	public List<Problem> findAll() {
		return this.problemRepository.findAll();

	}

	public Problem save(Problem problem) {
		return this.problemRepository.save(problem);
	}

	public void delete(Problem problem) {
		this.problemRepository.delete(problem);
	}

	public void flush() {
		this.problemRepository.flush();
	}

	//----------------------------CREATE/EDIT POSITION----------------------------------------------------------------------------
	//----------------------------------------------------------------------------------------------------------------------------
	public List<Problem> reconstructList(FormObjectPositionProblemCheckbox formObjectPositionProblemCheckbox) {

		List<Integer> ids = formObjectPositionProblemCheckbox.getProblems();

		Company loggedCompany = this.companyService.loggedCompany();

		List<Problem> problems = new ArrayList<>();

		if(ids!=null) {
		for (Integer id : ids) {

			Problem problem = this.problemRepository.findOne(id);
			Assert.notNull(problem);
			Assert.isTrue(loggedCompany.getProblems().contains(problem));

			problems.add(problem);

			}
		}
		return problems;
	}

	public List<Problem> showProblems() {
		Company company = this.companyService.loggedCompany();

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
		problem.setIsDraftMode(true);
		problem.setStatement("");

		return problem;
	}

	public void addAttachment(String attachment, Problem problem) {
		Assert.isTrue(this.companyService.loggedCompany().getProblems().contains(problem) && problem.getIsDraftMode());
		List<String> attachments = problem.getAttachments();

		if (!attachment.trim().isEmpty() && this.isUrl(attachment) && !attachments.contains(attachment)) {
			attachments.add(attachment);
			problem.setAttachments(attachments);
			this.save(problem);
		}


	}

	public Boolean isUrl(String url) {
		try {
			new URL(url).toURI();
			return true;
		} catch (Exception e) {
			return false;
		}
	}



	public Problem reconstruct(Problem problem, BindingResult binding) {
		Problem result = new Problem();

		if (problem.getId() == 0) {
			result = problem;
		} else {
			Problem copy = this.findOne(problem.getId());

			result.setVersion(copy.getVersion());
			result.setTitle(problem.getTitle());
			result.setAttachments(copy.getAttachments());
			result.setHint(problem.getHint());
			result.setStatement(problem.getStatement());
			result.setIsDraftMode(problem.getIsDraftMode());

			result.setId(copy.getId());
		}
		this.validator.validate(result, binding);
		return result;
	}

	public void updateProblem(Problem problem) {
		Problem pro = this.findOne(problem.getId());
		Company loggedCompany = this.companyService.loggedCompany();
		Assert.isTrue(loggedCompany.getProblems().contains(problem) && pro.getIsDraftMode());
		this.save(problem);

	}

	public void deleteProblem(Problem problem) {
		Company loggedCompany = this.companyService.loggedCompany();

		Assert.isTrue(loggedCompany.getProblems().contains(problem));
		Assert.isTrue(problem.getIsDraftMode());

		loggedCompany.getProblems().remove(problem);

		this.problemRepository.delete(problem);

	}

	public void removeAttachment(Problem problem, int attachmentNumber) {

		List<String> attachments = problem.getAttachments();
		Assert.isTrue(this.companyService.loggedCompany().getProblems().contains(problem) && attachments.size() > attachmentNumber && problem.getIsDraftMode());

		attachments.remove(attachmentNumber);
		problem.setAttachments(attachments);
		this.save(problem);
	}

	public void deleteInBatch(List<Problem> problems) {
		this.problemRepository.deleteInBatch(problems);

	}

}
