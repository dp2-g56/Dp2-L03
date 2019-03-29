
package domain;

import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class Problem extends DomainEntity {

	private String			title;
	private String			statement;
	private String			hint;
	private List<String>	attachment;
	private Boolean			isDraftMode;


	@NotBlank
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@NotBlank
	public String getStatement() {
		return this.statement;
	}

	public void setStatement(final String statement) {
		this.statement = statement;
	}

	@Valid
	public String getHint() {
		return this.hint;
	}

	public void setHint(final String hint) {
		this.hint = hint;
	}

	@Valid
	public List<String> getAttachment() {
		return this.attachment;
	}

	public void setAttachment(final List<String> attachment) {
		this.attachment = attachment;
	}

	@NotNull
	public Boolean getIsDraftMode() {
		return this.isDraftMode;
	}

	public void setIsDraftMode(final Boolean isDraftMode) {
		this.isDraftMode = isDraftMode;
	}

}
