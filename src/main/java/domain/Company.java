
package domain;

import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class Company extends Actor {

	private String			companyNumber;

	private List<Problem>	problems;
	private List<Position>	positions;


	@NotBlank
	public String getCompanyNumber() {
		return this.companyNumber;
	}

	public void setCompanyNumber(final String companyNumber) {
		this.companyNumber = companyNumber;
	}

	@OneToMany
	public List<Problem> getProblems() {
		return this.problems;
	}

	public void setProblems(final List<Problem> problems) {
		this.problems = problems;
	}

	@OneToMany
	public List<Position> getPositions() {
		return this.positions;
	}

	public void setPositions(final List<Position> positions) {
		this.positions = positions;
	}

}
