package com.googlecloudintegration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.googlecloudintegration.entity.InputFileEntity;

@Repository
public interface FileRepository extends JpaRepository<InputFileEntity, String> {
    
}
