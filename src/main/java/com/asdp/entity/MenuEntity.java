package com.asdp.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFilter;

@Entity(name = "Menu")
@Table(name = "sys_menu")
@JsonFilter(MenuEntity.Constant.JSON_FILTER)
public class MenuEntity extends AuditEntity implements Serializable{

	private static final long serialVersionUID = -6786776313367723074L;
	@Id
	private String menuCode;
	private String menuName;
	private String url;
	private String parent;
	
	@OneToMany(mappedBy = "menu")
	private Set<MenuRoleEntity> menuRole;
	
	public Set<MenuRoleEntity> getMenuRole() {
		return menuRole;
	}
	public void setMenuRole(Set<MenuRoleEntity> menuRole) {
		this.menuRole = menuRole;
	}
	public String getMenuCode() {
		return menuCode;
	}
	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public static class Constant {
		private Constant() {}
		public static final String MENU_CODE_FIELD = "menuCode";
		public static final String MENU_NAME_FIELD = "menuName";
		public static final String URL_FIELD = "url";
		public static final String PARENT_FIELD = "parent";
		public static final String MENU_ROLES_FIELD = "menuRole";
		public static final String JSON_FILTER = "jsonFilterMenu";
	}
	
}
