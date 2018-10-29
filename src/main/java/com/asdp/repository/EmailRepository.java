package com.asdp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asdp.entity.EmailEntity;

public interface EmailRepository  extends JpaRepository<EmailEntity, String> {

}
