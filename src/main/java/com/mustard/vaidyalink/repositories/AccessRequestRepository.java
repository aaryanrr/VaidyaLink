package com.mustard.vaidyalink.repositories;

import com.mustard.vaidyalink.entities.AccessRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccessRequestRepository extends JpaRepository<AccessRequest, UUID> {
}
