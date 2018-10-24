package com.asdp.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFilter;

@Entity(name = "MenuRole")
@Table(name = "sys_menu_role")
@JsonFilter(MenuRoleEntity.Constant.JSON_FILTER)
public class MenuRoleEntity  extends AuditEntity implements Serializable{

	private static final long serialVersionUID = 266610560287912683L;
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="user_role")
	private UserRoleEntity userRole;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="menu")
	private MenuEntity menu;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public UserRoleEntity getUserRole() {
		return userRole;
	}
	public void setUserRole(UserRoleEntity userRole) {
		this.userRole = userRole;
	}
	public MenuEntity getMenu() {
		return menu;
	}
	public void setMenu(MenuEntity menu) {
		this.menu = menu;
	}
	public static class Constant {
		private Constant() {}
		public static final String USER_ROLE_FIELD = "userRole";
		public static final String JSON_FILTER = "jsonFilterMenuRole";
	}
	

}
