package com.asdp.service;

import java.util.List;

import com.asdp.entity.MenuEntity;

public interface MenuService {
	List<MenuEntity> getMenuByUser(String userName);
}
