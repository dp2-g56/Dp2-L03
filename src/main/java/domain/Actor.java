
package domain;

import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import security.UserAccount;

@Entity
@Access(AccessType.PROPERTY)
public class Actor extends DomainEntity {

	private String				name;
	private String				surname;
	private String				VATNumber;
	private CreditCard			creditCard;
	private String				photo;
	private String				email;
	private String				phone;
	private String				address;

	private List<SocialProfile>	socialProfiles;
	private List<Message>		messages;

	private UserAccount			userAccount;


	@NotBlank
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@NotBlank
	public String getSurname() {
		return this.surname;
	}

	public void setSurname(final String surname) {
		this.surname = surname;
	}

	@NotBlank
	public String getVATNumber() {
		return this.VATNumber;
	}

	public void setVATNumber(final String vATNumber) {
		this.VATNumber = vATNumber;
	}

	@NotNull
	@Valid
	public CreditCard getCreditCard() {
		return this.creditCard;
	}

	public void setCreditCard(final CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	@URL
	public String getPhoto() {
		return this.photo;
	}

	public void setPhoto(final String photo) {
		this.photo = photo;
	}

	@Pattern(regexp = "[\\w.%-]+\\@[-.\\w]+\\.[A-Za-z]{2,4}|[\\w.%-]+\\<+[\\w.%-]+\\@[-.\\w]+\\.[A-Za-z]{2,4}|[\\w.%-]+\\<[\\w.%-]+\\@+\\>|[\\w.%-]+",
		message = "Email doesn't follow the pattern, must be identifier@domain.asd or alias <identifier@domain.asd>")
	public String getEmail() {
		return this.email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	@Valid
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(final String phone) {
		this.phone = phone;
	}

	@Valid
	public String getAddress() {
		return this.address;
	}

	public void setAddress(final String address) {
		this.address = address;
	}

	@OneToMany(cascade = CascadeType.ALL)
	@Valid
	public List<SocialProfile> getSocialProfiles() {
		return this.socialProfiles;
	}

	public void setSocialProfiles(final List<SocialProfile> socialProfiles) {
		this.socialProfiles = socialProfiles;
	}

	@OneToMany(cascade = CascadeType.ALL)
	@Valid
	public List<Message> getMessages() {
		return this.messages;
	}

	public void setMessages(final List<Message> messages) {
		this.messages = messages;
	}

	@OneToOne(optional = false, cascade = CascadeType.ALL)
	@Valid
	public UserAccount getUserAccount() {
		return this.userAccount;
	}

	public void setUserAccount(final UserAccount userAccount) {
		this.userAccount = userAccount;
	}
}
