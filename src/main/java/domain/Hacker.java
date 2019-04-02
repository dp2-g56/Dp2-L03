
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

	private List<Curriculum>	curriculums;
	private List<Application>	applications;
	private Finder				finder;


	@Valid
	@OneToMany(cascade = CascadeType.ALL)
	public List<Curriculum> getCurriculums() {
		return this.curriculums;
	}

	public void setCurriculums(List<Curriculum> curriculums) {
		this.curriculums = curriculums;

	}

	@Valid
	@OneToMany(mappedBy = "hacker")
	public List<Application> getApplications() {
		return this.applications;
	}

	public void setApplications(List<Application> applications) {
		this.applications = applications;
	}

	@OneToOne(optional = true, cascade = CascadeType.ALL)
	@Valid
	public Finder getFinder() {
		return this.finder;
	}

	public void setFinder(Finder finder) {
		this.finder = finder;
	}
}
