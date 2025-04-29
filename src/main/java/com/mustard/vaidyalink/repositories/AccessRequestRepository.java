package com.mustard.vaidyalink.repositories;

import com.mustard.vaidyalink.entities.AccessRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

public interface AccessRequestRepository extends JpaRepository<AccessRequest, UUID> {

    @NotNull Optional<AccessRequest> findById(@NotNull UUID id);

    List<AccessRequest> findAllByAadhaarNumber(String aadhaarNumber);

    List<AccessRequest> findAllByInstitutionRegistrationNumber(String institutionRegistrationNumber);

    List<AccessRequest> findAllByTimePeriodBeforeAndApprovedIsTrue(LocalDate timePeriod);

    List<AccessRequest> findAllByInstitutionRegistrationNumberAndApprovedIsTrue(String regNum);
}
