package com.mustard.vaidyalink.repositories;

import com.mustard.vaidyalink.entities.InstitutionAccessData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface InstitutionAccessDataRepository extends JpaRepository<InstitutionAccessData, UUID> {
    List<InstitutionAccessData> findAllByInstitutionRegistrationNumber(String institutionRegistrationNumber);

    List<InstitutionAccessData> findAllByAccessRequestId(UUID accessRequestId);
}
