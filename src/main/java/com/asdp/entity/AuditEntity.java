package com.asdp.entity;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
public class AuditEntity implements Serializable {

	private static final long serialVersionUID = -117659353674015029L;
	@CreatedBy
	@Column(updatable = false)
	private String createdBy;
	@CreatedDate
	@Column(updatable = false)
	private Date createdDate;
	@LastModifiedBy
	private String modifiedBy;
	@LastModifiedDate
	private Date modifiedDate;

	public String getCreatedBy() {
		return this.createdBy;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public Date getModifiedDate() {
		return this.modifiedDate;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
}