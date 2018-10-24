package com.asdp.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFilter;


@Entity(name = "UserRole")
@Table(name = "sys_user_role")
@JsonFilter(UserRoleEntity.Constant.JSON_FILTER)
public class UserRoleEntity extends AuditEntity implements Serializable{

	private static final long serialVersionUID = 886950508881845834L;
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String userRoleCode;
	private String roleName;
	
	@OneToMany(mappedBy = UserEntity.Constant.USER_ROLE_FIELD)
	private Set<UserEntity> user;
	
	@OneToMany(mappedBy = "userRole")
	private Set<MenuRoleEntity> menuRole;
	
	public Set<MenuRoleEntity> getMenuRole() {
		return menuRole;
	}
	public void setMenuRole(Set<MenuRoleEntity> menuRole) {
		this.menuRole = menuRole;
	}
	public Set<UserEntity> getUser() {
		return user;
	}
	public void setUser(Set<UserEntity> user) {
		this.user = user;
	}
	public String getUserRoleCode() {
		return userRoleCode;
	}
	public void setUserRoleCode(String userRoleCode) {
		this.userRoleCode = userRoleCode;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}	
	public static class Constant {
		private Constant() {}
		public static final String USER_CODE_FIELD = "userRoleCode";
		public static final String JSON_FILTER = "jsonFilterUserRole";
	}
	
	
}
