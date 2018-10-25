package com.asdp.repository;

import org.springframework.data.repository.CrudRepository;
import com.asdp.entity.ResponseMappingEntity;

public interface ResponseMappingRepo extends CrudRepository<ResponseMappingEntity, String> {
}
