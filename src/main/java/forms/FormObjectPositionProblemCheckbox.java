
package forms;

import java.util.Date;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

public class FormObjectPositionProblemCheckbox {

	//Atributos de Position
	private int				id;
	private String			title;
	private String			description;
	private Date			deadline;
	private String			requiredProfile;
	private String			requiredSkills;		//It will be converted into a List
	private String			requiredTecnologies;	//It will be converted into a List
	private Double			offeredSalary;
	private Boolean			isDraftMode;

	//Atributos de problems
	private List<Integer>	problems;


	@NotNull
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@NotBlank
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@NotBlank
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Future
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	@NotNull
	public Date getDeadline() {
		return this.deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	@NotBlank
	public String getRequiredProfile() {
		return this.requiredProfile;
	}

	public void setRequiredProfile(String requiredProfile) {
		this.requiredProfile = requiredProfile;
	}

	@ElementCollection(targetClass = String.class)
	@NotBlank
	public String getRequiredSkills() {
		return this.requiredSkills;
	}

	public void setRequiredSkills(String requiredSkills) {
		this.requiredSkills = requiredSkills;
	}

	@ElementCollection(targetClass = String.class)
	@NotBlank
	public String getRequiredTecnologies() {
		return this.requiredTecnologies;
	}

	public void setRequiredTecnologies(String requiredTecnologies) {
		this.requiredTecnologies = requiredTecnologies;
	}

	@Min(0)
	@NotNull
	public Double getOfferedSalary() {
		return this.offeredSalary;
	}

	public void setOfferedSalary(Double offeredSalary) {
		this.offeredSalary = offeredSalary;
	}

	@ElementCollection(targetClass = Integer.class)
	public List<Integer> getProblems() {
		return this.problems;
	}

	public void setProblems(List<Integer> problems) {
		this.problems = problems;
	}

	@NotNull
	public Boolean getIsDraftMode() {
		return this.isDraftMode;
	}

	public void setIsDraftMode(Boolean isDraftMode) {
		this.isDraftMode = isDraftMode;
	}

}
