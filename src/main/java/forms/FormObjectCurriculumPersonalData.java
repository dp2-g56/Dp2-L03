
package forms;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public class FormObjectCurriculumPersonalData {

	//Atributos de Curriculum
	private int		id;
	private String	title;

	//Atributos de Personal Data
	private String fullName;
	private String statement;
	private String phoneNumber;
	private String linkedInProfile;
	private String gitHubProfile;
	
	public FormObjectCurriculumPersonalData() {
		this.id = 0;
	}

	@NotNull
	@Valid
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@NotBlank
	@Valid
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@NotBlank
	@Valid
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@NotBlank
	@Valid
	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}

	@Valid
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	@URL
	@NotBlank
	@Valid
	public String getGitHubProfile() {
		return this.gitHubProfile;
	}

	public void setGitHubProfile(String gitHubProfile) {
		this.gitHubProfile = gitHubProfile;
	}

	@URL
	@NotBlank
	@Valid
	public String getLinkedInProfile() {
		return linkedInProfile;
	}

	public void setLinkedInProfile(String linkedInProfile) {
		this.linkedInProfile = linkedInProfile;
	}

}
