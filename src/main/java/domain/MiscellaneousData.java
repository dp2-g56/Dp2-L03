
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class MiscellaneousData extends DomainEntity {

	private String	freeText;
	private String	attachment;


	@NotBlank
	public String getFreeText() {
		return this.freeText;
	}

	public void setFreeText(String freeText) {
		this.freeText = freeText;
	}

	public String getAttachment() {
		return this.attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

}
