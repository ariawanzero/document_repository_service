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

@Entity(name = "User")
@Table(name = "sys_user")
@JsonFilter(UserEntity.Constant.JSON_FILTER)
public class UserEntity extends AuditEntity implements Serializable {
	
	private static final long serialVersionUID = 2371860388537337695L;
	@Id
	@GeneratedValue(generator = "code-uuid")
	@GenericGenerator(name = "code-uuid", strategy = "uuid")
	private String id;
	private String username;
	private String password;
	private String name;
	private String noHp;
	private String alamat;
	private String jabatan;
	private String divisi;
	private String unit;
	private Date expiredDate;
	private Integer valid = 1;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="user_role")
	private UserRoleEntity userRole;
	
	@Transient
	private String userRoleName;
	@Transient
	private String userRoleId;
	@Transient
	private String position;
	@Transient
	private String status;
	
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getUserRoleId() {
		return userRoleId;
	}
	public void setUserRoleId(String userRoleId) {
		this.userRoleId = userRoleId;
	}
	public String getUserRoleName() {
		return userRoleName;
	}
	public void setUserRoleName(String userRoleName) {
		this.userRoleName = userRoleName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public UserRoleEntity getUserRole() {
		return userRole;
	}
	public void setUserRole(UserRoleEntity userRole) {
		this.userRole = userRole;
	}
	
	public String getNoHp() {
		return noHp;
	}
	public void setNoHp(String noHp) {
		this.noHp = noHp;
	}
	public String getAlamat() {
		return alamat;
	}
	public void setAlamat(String alamat) {
		this.alamat = alamat;
	}
	public String getJabatan() {
		return jabatan;
	}
	public void setJabatan(String jabatan) {
		this.jabatan = jabatan;
	}
	public String getDivisi() {
		return divisi;
	}
	public void setDivisi(String divisi) {
		this.divisi = divisi;
	}
	public Date getExpiredDate() {
		return expiredDate;
	}
	public void setExpiredDate(Date expiredDate) {
		this.expiredDate = expiredDate;
	}
	public Integer getValid() {
		return valid;
	}
	public void setValid(Integer valid) {
		this.valid = valid;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public static class Constant {
		private Constant() {}
		public static final String ID_FIELD = "id";
		public static final String USERNAME_FIELD = "username";
		public static final String USER_ROLE_FIELD = "userRole";
		public static final String NAME_FIELD = "name";
		public static final String VALID_FIELD = "valid";
		public static final String PASSWORD_FIELD = "password";
		public static final String JABATAN_FIELD = "jabatan";
		public static final String DIVISI_FIELD = "divisi";
		public static final String EXPIRED_DATE_FIELD = "expiredDate";
		public static final String JSON_FILTER = "jsonFilterUser";
	}
}
