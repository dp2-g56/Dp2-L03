
package domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
		@Index(columnList = "title, description, ticker, requiredProfile, isDraftMode"), 
		@Index(columnList = "deadline, isDraftMode"),
		@Index(columnList = "offeredSalary, isDraftMode"),
		@Index(columnList = "isDraftMode")
	})
public class Position extends DomainEntity {

	private String				title;
	private String				description;
	private Date				deadline;
	private String				requiredProfile;
	private List<String>		requiredSkills;
	private List<String>		requiredTecnologies;
	private Double				offeredSalary;
	private String				ticker;
	private Boolean				isDraftMode;
	private Boolean				isCancelled;

	private List<Problem>		problems;

	private List<Application>	applications;


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
	@NotEmpty
	public List<String> getRequiredSkills() {
		return this.requiredSkills;
	}

	public void setRequiredSkills(List<String> requiredSkills) {
		this.requiredSkills = requiredSkills;
	}

	@ElementCollection(targetClass = String.class)
	@NotEmpty
	public List<String> getRequiredTecnologies() {
		return this.requiredTecnologies;
	}

	public void setRequiredTecnologies(List<String> requiredTecnologies) {
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

	@Pattern(regexp = "([A-Za-z]{4})-([0-9]{4})")
	public String getTicker() {
		return this.ticker;
	}

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

	@NotNull
	public Boolean getIsDraftMode() {
		return this.isDraftMode;
	}

	public void setIsDraftMode(Boolean isDraftMode) {
		this.isDraftMode = isDraftMode;
	}

	@NotNull
	public Boolean getIsCancelled() {
		return this.isCancelled;
	}

	public void setIsCancelled(Boolean isCancelled) {
		this.isCancelled = isCancelled;
	}

	@ManyToMany
	@Valid
	public List<Problem> getProblems() {
		return this.problems;
	}

	public void setProblems(List<Problem> problems) {
		this.problems = problems;
	}

	@OneToMany(mappedBy = "position")
	public List<Application> getApplications() {
		return this.applications;
	}

	public void setApplications(List<Application> applications) {
		this.applications = applications;
	}

}
