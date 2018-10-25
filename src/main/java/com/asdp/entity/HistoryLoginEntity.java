package com.asdp.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFilter;

@Entity(name = "HistoryLogin")
@Table(name = "sys_history_login")
@JsonFilter(HistoryLoginEntity.Constant.JSON_FILTER)
public class HistoryLoginEntity implements Serializable{

	private static final long serialVersionUID = 886950508881845835L;
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="user")
	private UserEntity user;
	private Date dateLogin;
	
	@Transient
	private String username;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public UserEntity getUser() {
		return user;
	}
	public void setUser(UserEntity user) {
		this.user = user;
	}
	public Date getDateLogin() {
		return dateLogin;
	}
	public void setDateLogin(Date dateLogin) {
		this.dateLogin = dateLogin;
	}
	
	public static class Constant {
		private Constant() {}
		public static final String ID_FIELD = "id";
		public static final String USER_FIELD = "user";
		public static final String DATE_LOGIN_FIELD = "dateLogin";
		public static final String JSON_FILTER = "jsonFilterHistoryLoginEntity";
	}
	
}
