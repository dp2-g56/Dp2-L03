
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Message extends DomainEntity {

	private Date	moment;
	private String	subject;
	private String	body;
	private String	tags;

	private String	sender;
	private String	receiver;


	public Message() {		//For Json purposes
		super();
	}

	@Past
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getMoment() {
		return this.moment;
	}

	public void setMoment(Date moment) {
		this.moment = moment;
	}

	@NotBlank
	public String getSubject() {
		return this.subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	@NotBlank
	public String getBody() {
		return this.body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getTags() {
		return this.tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	@NotBlank
	public String getSender() {
		return this.sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	@NotBlank
	public String getReceiver() {
		return this.receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

}
