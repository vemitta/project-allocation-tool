package com.theja.projectallocationservice.services;

import com.theja.projectallocationservice.exceptions.ResourceNotFoundException;
import com.theja.projectallocationservice.models.*;
import com.theja.projectallocationservice.repositories.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;
//    public List<DBApplication> getAllApplications() {
//        return applicationRepository.findAll();
//    }

    public Page<DBApplication> getAllApplicationsByStatus(ApplicationStatus status, Pageable pageable) {
        if (status == null) {
            return applicationRepository.findAll(pageable);
        } else {
            return applicationRepository.findByStatus(status, pageable);
        }
    }



    public DBApplication getApplicationById(Long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
    }
    public DBApplication createApplication(DBApplication application) {
        return applicationRepository.save(application);
    }

    public DBApplication updateApplication(Long id, DBApplication application) {
        DBApplication existingApplication = getApplicationById(id);
        // Update the properties of existingApplication with the properties from the provided application
        existingApplication.setStatus(application.getStatus());
        existingApplication.setAppliedAt(application.getAppliedAt());
        existingApplication.setOpening(application.getOpening());
        return applicationRepository.save(existingApplication);
    }

    public void deleteApplication(Long id) {
        if (applicationRepository.existsById(id)) {
            applicationRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Application not found");
        }
    }

    // Implement the method to retrieve application by opening ID and candidate ID
    public DBApplication getApplicationByOpeningIdAndCandidateId(Long openingId, Long candidateId) {
        return applicationRepository.findByOpeningIdAndCandidateId(openingId, candidateId);
    }
}

