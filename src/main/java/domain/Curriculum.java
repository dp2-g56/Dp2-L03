
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

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class Curriculum extends DomainEntity {

	private String					title;

	private PersonalData			personalData;
	private List<PositionData>		positionData;
	private List<EducationData>		educationData;
	private List<MiscellaneousData>	miscellaneousData;

	@NotBlank
	@Valid
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@OneToOne(cascade = CascadeType.ALL)
	@Valid
	@NotNull
	public PersonalData getPersonalData() {
		return this.personalData;
	}

	public void setPersonalData(PersonalData personalData) {
		this.personalData = personalData;
	}

	@OneToMany(cascade = CascadeType.ALL)
	@Valid
	public List<PositionData> getPositionData() {
		return this.positionData;
	}

	public void setPositionData(List<PositionData> positionData) {
		this.positionData = positionData;
	}

	@OneToMany(cascade = CascadeType.ALL)
	@Valid
	public List<EducationData> getEducationData() {
		return this.educationData;
	}

	public void setEducationData(List<EducationData> educationData) {
		this.educationData = educationData;
	}

	@OneToMany(cascade = CascadeType.ALL)
	@Valid
	public List<MiscellaneousData> getMiscellaneousData() {
		return this.miscellaneousData;
	}

	public void setMiscellaneousData(List<MiscellaneousData> miscellaneousData) {
		this.miscellaneousData = miscellaneousData;
	}
}
