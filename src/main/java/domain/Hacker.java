
package domain;

import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.Valid;

@Entity
@Access(AccessType.PROPERTY)
public class Hacker extends Actor {

	private Curriculum			curriculum;
	private List<Application>	applications;
	private Finder				finder;


	@OneToOne(optional = true, cascade = CascadeType.ALL)
	@Valid
	public Curriculum getCurriculum() {
		return this.curriculum;
	}

	public void setCurriculum(final Curriculum curriculum) {
		this.curriculum = curriculum;
	}

	@Valid
	@OneToMany(mappedBy = "hacker")
	public List<Application> getApplications() {
		return this.applications;
	}

	public void setApplications(final List<Application> applications) {
		this.applications = applications;
	}

	@OneToOne(optional = true, cascade = CascadeType.ALL)
	@Valid
	public Finder getFinder() {
		return this.finder;
	}

	public void setFinder(final Finder finder) {
		this.finder = finder;
	}
}
