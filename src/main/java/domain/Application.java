
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Application extends DomainEntity {

	private Date creationMoment;
	private String link;
	private String explication;
	private Date submitMoment;
	private Status status;

	private Problem problem;
	private Position position;
	private Curriculum curriculum;
	private Hacker hacker;

	@Past
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	@NotNull
	public Date getCreationMoment() {
		return this.creationMoment;
	}

	public void setCreationMoment(Date creationMoment) {
		this.creationMoment = creationMoment;
	}

	@URL
	@Valid
	public String getLink() {
		return this.link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	@Valid
	public String getExplication() {
		return this.explication;
	}

	public void setExplication(String explication) {
		this.explication = explication;
	}

	@Past
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getSubmitMoment() {
		return this.submitMoment;
	}

	public void setSubmitMoment(Date submitMoment) {
		this.submitMoment = submitMoment;
	}

	@NotNull
	@Enumerated(EnumType.STRING)
	public Status getStatus() {
		return this.status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@ManyToOne
	public Problem getProblem() {
		return this.problem;
	}

	public void setProblem(Problem problem) {
		this.problem = problem;
	}

	@ManyToOne
	public Position getPosition() {
		return this.position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	@OneToOne(cascade = CascadeType.ALL)
	public Curriculum getCurriculum() {
		return this.curriculum;
	}

	public void setCurriculum(Curriculum curriculum) {
		this.curriculum = curriculum;
	}

	@ManyToOne
	public Hacker getHacker() {
		return this.hacker;
	}

	public void setHacker(Hacker hacker) {
		this.hacker = hacker;
	}
}
