package com.theja.projectallocationservice.repositories;

import com.theja.projectallocationservice.models.ApplicationStatus;
import com.theja.projectallocationservice.models.DBApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<DBApplication, Long> {
    DBApplication findByOpeningIdAndCandidateId(Long openingId, Long candidateId);
    // Custom query methods or additional repository operations can be defined here if needed

    Page<DBApplication> findByStatus(ApplicationStatus status, Pageable pageable);
}

