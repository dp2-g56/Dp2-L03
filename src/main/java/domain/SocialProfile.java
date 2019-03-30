
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class SocialProfile extends DomainEntity {

	private String	nick;
	private String	name;
	private String	profileLink;


	public SocialProfile() {		//Created for Json purposes
		super();
	}
	@NotBlank
	public String getNick() {
		return this.nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	@NotBlank
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@NotBlank
	@URL
	public String getProfileLink() {
		return this.profileLink;
	}

	public void setProfileLink(String profileLink) {
		this.profileLink = profileLink;
	}

}
