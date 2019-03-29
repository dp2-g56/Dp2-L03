
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.validation.constraints.Future;
import javax.validation.constraints.Min;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Finder extends DomainEntity {

	private String	keyWord;
	private Date	deadLine;
	private Double	minSalary;
	private Date	maxDeadLine;


	public String getKeyWord() {
		return this.keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	@Future
	public Date getDeadLine() {
		return this.deadLine;
	}

	public void setDeadLine(Date deadLine) {
		this.deadLine = deadLine;
	}

	@Min(0)
	public Double getMinSalary() {
		return this.minSalary;
	}

	public void setMinSalary(Double minSalary) {
		this.minSalary = minSalary;
	}

	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	@Future
	public Date getMaxDeadLine() {
		return this.maxDeadLine;
	}

	public void setMaxDeadLine(Date maxDeadLine) {
		this.maxDeadLine = maxDeadLine;
	}

}
