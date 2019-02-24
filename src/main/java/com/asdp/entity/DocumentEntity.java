package com.asdp.entity;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.asdp.util.JsonUtil;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Entity(name = "Document")
@Table(name = "document")
@JsonFilter(DocumentEntity.Constant.JSON_FILTER)
public class DocumentEntity extends AuditEntity implements Serializable {

	private static final long serialVersionUID = -8789641666442140622L;
	@Id
	@GeneratedValue(generator = "code-uuid")
	@GenericGenerator(name = "code-uuid", strategy = "uuid")
	private String id;
	private String name;
	private String description;
	private String descriptionNoTag;
	private String type;
	private String sop;
	private String category;
	private String divisi;
	private String tumbnail;
	private Date startDate;
	private Date endDate;
	private String nameFileJson;
	private String status;
	private String reason;
	private boolean twitter = false;
	private boolean facebook = false;
	private boolean instagram = false;	
	private Integer valid = 1;
	
	@Transient
	private List<String> nameFile;
	@Transient
	private String urlPreview;
	@Transient
	private int countRead;
	@Transient
	private String startDateDisplay;
	@Transient
	private String endDateDisplay;
	@Transient
	private String createdDateDisplay;
	@Transient
	private boolean view = false;
	@Transient
	private String descriptionNoTagShow;
	@Transient
	private int countHist;
	
	public int getCountHist() {
		return countHist;
	}
	public void setCountHist(int countHist) {
		this.countHist = countHist;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getDescriptionNoTagShow() {
		return descriptionNoTagShow;
	}
	public void setDescriptionNoTagShow(String descriptionNoTagShow) {
		this.descriptionNoTagShow = descriptionNoTagShow;
	}
	public String getCreatedDateDisplay() {
		return createdDateDisplay;
	}
	public void setCreatedDateDisplay(String createdDateDisplay) {
		this.createdDateDisplay = createdDateDisplay;
	}
	public String getStartDateDisplay() {
		return startDateDisplay;
	}
	public void setStartDateDisplay(String startDateDisplay) {
		this.startDateDisplay = startDateDisplay;
	}
	public String getEndDateDisplay() {
		return endDateDisplay;
	}
	public void setEndDateDisplay(String endDateDisplay) {
		this.endDateDisplay = endDateDisplay;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public boolean isView() {
		return view;
	}
	public void setView(boolean view) {
		this.view = view;
	}
	public String getDescriptionNoTag() {
		return descriptionNoTag;
	}
	public void setDescriptionNoTag(String descriptionNoTag) {
		this.descriptionNoTag = descriptionNoTag;
	}
	public String getSop() {
		return sop;
	}
	public void setSop(String sop) {
		this.sop = sop;
	}
	public boolean isTwitter() {
		return twitter;
	}
	public void setTwitter(boolean twitter) {
		this.twitter = twitter;
	}
	public boolean isFacebook() {
		return facebook;
	}
	public void setFacebook(boolean facebook) {
		this.facebook = facebook;
	}
	public boolean isInstagram() {
		return instagram;
	}
	public void setInstagram(boolean instagram) {
		this.instagram = instagram;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getDivisi() {
		return divisi;
	}
	public void setDivisi(String divisi) {
		this.divisi = divisi;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getNameFileJson() {
		return nameFileJson;
	}
	public void setNameFileJson(String nameFileJson) {
		this.nameFileJson = nameFileJson;
	}
	public Integer getValid() {
		return valid;
	}
	public void setValid(Integer valid) {
		this.valid = valid;
	}
	@SuppressWarnings("unchecked")
	public List<String> getNameFile() throws JsonParseException, JsonMappingException, IOException {
		if(getNameFileJson() != null) {
			this.nameFile = JsonUtil.parseJson(getNameFileJson(), List.class);
		}
		return nameFile;
	}
	public void setNameFile(List<String> nameFile) {
		this.nameFile = nameFile;
	}
	public String getUrlPreview() {
		return urlPreview;
	}
	public void setUrlPreview(String urlPreview) {
		this.urlPreview = urlPreview;
	}
	public int getCountRead() {
		return countRead;
	}
	public void setCountRead(int countRead) {
		this.countRead = countRead;
	}
	public String getTumbnail() {
		return tumbnail;
	}
	public void setTumbnail(String tumbnail) {
		this.tumbnail = tumbnail;
	}
	
	public static class Constant {
		private Constant() {}
		public static final String ID_FIELD = "id";
		public static final String NAME_FIELD = "name";
		public static final String DESCRIPTION_FIELD = "description";
		public static final String DESCRIPTION_NO_TAG_FIELD = "descriptionNoTag";
		public static final String DIVISI_FIELD = "divisi";
		public static final String STATUS_FIELD = "status";
		public static final String START_DATE_FIELD = "startDate";
		public static final String END_DATE_FIELD = "endDate";
		public static final String TYPE_FIELD = "type";
		public static final String VALID_FIELD = "valid";
		public static final String CATEGORY_FIELD = "category";
		public static final String TUMBNAIL_FIELD = "tumbnail";
		public static final String NAME_FILE_FIELD = "nameFile";
		public static final String CREATED_BY_FIELD = "createdBy";
		public static final String NAME_FILE_JSON_FIELD = "nameFileJson";
		public static final String URL_PREVIEW_FIELD = "urlPreview";
		public static final String JSON_FILTER = "jsonFilterDocument";
	}

}
