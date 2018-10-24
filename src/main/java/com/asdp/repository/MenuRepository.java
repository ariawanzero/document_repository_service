package com.asdp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asdp.entity.MenuEntity;

public interface MenuRepository extends JpaRepository<MenuEntity, String> {

}
