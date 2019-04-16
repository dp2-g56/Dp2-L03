
package domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.Past;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Finder extends DomainEntity {

	private String	keyWord;
	private Date	deadLine;
	private Double	minSalary;
	private Date	maxDeadLine;
	
	private Date lastEdit;
	private List<Position> positions;


	@Valid
	public String getKeyWord() {
		return this.keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	@Future
	public Date getDeadLine() {
		return this.deadLine;
	}

	public void setDeadLine(Date deadLine) {
		this.deadLine = deadLine;
	}

	@Valid
	public Double getMinSalary() {
		return this.minSalary;
	}

	public void setMinSalary(Double minSalary) {
		this.minSalary = minSalary;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	@Future
	public Date getMaxDeadLine() {
		return this.maxDeadLine;
	}

	public void setMaxDeadLine(Date maxDeadLine) {
		this.maxDeadLine = maxDeadLine;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Valid
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getLastEdit() {
		return this.lastEdit;
	}

	public void setLastEdit(final Date lastEdit) {
		this.lastEdit = lastEdit;
	}

	@Valid
	@ManyToMany
	public List<Position> getPositions() {
		return positions;
	}

	public void setPositions(List<Position> positions) {
		this.positions = positions;
	}	

}
