
package domain;

import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.Valid;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class Company extends Actor {

	private String			companyName;

	private List<Problem>	problems;
	private List<Position>	positions;


	@NotBlank
	public String getCompanyName() {
		return this.companyName;
	}


	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	@OneToMany
	@Valid
	public List<Problem> getProblems() {
		return this.problems;
	}

	public void setProblems(List<Problem> problems) {
		this.problems = problems;
	}

	@OneToMany
	@Valid
	public List<Position> getPositions() {
		return this.positions;
	}

	public void setPositions(List<Position> positions) {
		this.positions = positions;
	}

}
