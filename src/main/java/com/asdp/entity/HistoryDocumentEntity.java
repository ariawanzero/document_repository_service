package com.asdp.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFilter;

@Entity(name = "HistoryDocument")
@Table(name = "history_document")
@JsonFilter(HistoryDocumentEntity.Constant.JSON_FILTER)
public class HistoryDocumentEntity  implements Serializable {
	
	private static final long serialVersionUID = -4505382576017318421L;
	@Id
	@GeneratedValue(generator = "code-uuid")
	@GenericGenerator(name = "code-uuid", strategy = "uuid")
	private String id;
	private String username;
	private String document;
	private Date readDocument;
	
	@Transient
	private String nameDocument;
	
	@Transient
	private String divisi;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public Date getReadDocument() {
		return readDocument;
	}

	public void setReadDocument(Date readDocument) {
		this.readDocument = readDocument;
	}

	public String getNameDocument() {
		return nameDocument;
	}

	public void setNameDocument(String nameDocument) {
		this.nameDocument = nameDocument;
	}

	public String getDivisi() {
		return divisi;
	}

	public void setDivisi(String divisi) {
		this.divisi = divisi;
	}
	
	public static class Constant {
		private Constant() {}
		public static final String ID_FIELD = "id";
		public static final String USERNAME_FIELD = "username";
		public static final String DOCUMENT_FIELD = "document";
		public static final String READ_DOCUMENT_FIELD = "readDocument";
		public static final String JSON_FILTER = "jsonFilterHistoryDocument";
	}
}
