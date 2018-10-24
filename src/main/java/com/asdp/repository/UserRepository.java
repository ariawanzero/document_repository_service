package com.asdp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.asdp.entity.UserEntity;

public interface UserRepository extends JpaSpecificationExecutor<UserEntity>, JpaRepository<UserEntity, String> {
	UserEntity findByUsername(String username);
}
