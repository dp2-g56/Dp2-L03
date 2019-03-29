
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
public class Curriculum extends DomainEntity {

	private String					title;

	private PersonalData			personalData;
	private List<PositionData>		positionData;
	private List<EducationData>		educationData;
	private List<MiscellaneousData>	miscellaneousData;


	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@OneToOne(optional = true, cascade = CascadeType.ALL)
	@Valid
	public PersonalData getPersonalData() {
		return this.personalData;
	}

	public void setPersonalData(final PersonalData personalData) {
		this.personalData = personalData;
	}

	@OneToMany(cascade = CascadeType.ALL)
	@Valid
	public List<PositionData> getPositionData() {
		return this.positionData;
	}

	public void setPositionData(final List<PositionData> positionData) {
		this.positionData = positionData;
	}

	@OneToMany(cascade = CascadeType.ALL)
	@Valid
	public List<EducationData> getEducationData() {
		return this.educationData;
	}

	public void setEducationData(final List<EducationData> educationData) {
		this.educationData = educationData;
	}

	@OneToMany(cascade = CascadeType.ALL)
	@Valid
	public List<MiscellaneousData> getMiscellaneousData() {
		return this.miscellaneousData;
	}

	public void setMiscellaneousData(final List<MiscellaneousData> miscellaneousData) {
		this.miscellaneousData = miscellaneousData;
	}
}
