package com.pilotbooking.repository;

import com.pilotbooking.domain.ServiceFacility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface ServiceFacilityRepository extends JpaRepository<ServiceFacility, UUID> {
    List<ServiceFacility> findByIsActiveTrue();

    long countByIsActiveTrue();
}