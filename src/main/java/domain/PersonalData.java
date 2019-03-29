
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class PersonalData extends DomainEntity {

	private String	fullName;

	private String	statement;
	private String	phoneNumber;
	private String	gitHubProfile;
	private String	linkedinProfile;


	@NotBlank
	public String getFullName() {
		return this.fullName;
	}

	public void setFullName(final String fullName) {
		this.fullName = fullName;
	}

	@NotBlank
	public String getStatement() {
		return this.statement;
	}

	public void setStatement(final String statement) {
		this.statement = statement;
	}

	@NotBlank
	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(final String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@URL
	@NotBlank
	public String getGitHubProfile() {
		return this.gitHubProfile;
	}

	public void setGitHubProfile(final String gitHubProfile) {
		this.gitHubProfile = gitHubProfile;
	}

	@URL
	@NotBlank
	public String getLinkedinProfile() {
		return this.linkedinProfile;
	}

	public void setLinkedinProfile(final String linkedinProfile) {
		this.linkedinProfile = linkedinProfile;
	}
}
