package com.urban.EmployeeManager.repository;

import com.urban.EmployeeManager.model.Office;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OfficeRepository extends JpaRepository<Office, Long> {
    Optional<Office> findByName(String name);
    boolean existsByName(String name);
}

