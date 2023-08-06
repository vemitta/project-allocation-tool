package com.theja.projectallocationservice.controllers;

import com.theja.projectallocationservice.mappers.ApplicationMapper;
import com.theja.projectallocationservice.models.*;
import com.theja.projectallocationservice.services.ApplicationService;
import com.theja.projectallocationservice.services.OpeningService;
import com.theja.projectallocationservice.services.ProjectService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class ApplicationController {
    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private OpeningService openingService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ApplicationMapper applicationMapper;

//    @GetMapping("/applications")
//    public ResponseEntity<List<Application>> getAllApplications() {
//        List<DBApplication> dbApplications = applicationService.getAllApplications();
//        return ResponseEntity.ok(applicationMapper.entityToModel(dbApplications));
//    }

    @GetMapping("/applications")
    public ResponseEntity<ApplicationListResponse> getAllApplications(
            @RequestParam(required = false) ApplicationStatus status,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<DBApplication> pageResult = applicationService.getAllApplicationsByStatus(status, pageable);

        List<Application> applicationList = applicationMapper.entityToModel(pageResult.getContent());

        ApplicationListResponse response = ApplicationListResponse.builder()
                .applications(applicationList)
                .totalElements(pageResult.getTotalElements())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/applications/{id}")
    public ResponseEntity<Application> getApplication(@PathVariable Long id) {
        DBApplication dbApplication = applicationService.getApplicationById(id);
        return ResponseEntity.ok(applicationMapper.entityToModel(dbApplication));
    }

    @GetMapping("/applications/details")
    public ResponseEntity<Application> getApplicationDetails(
            @RequestParam Long openingId,
            @RequestParam Long candidateId
    ) {
        DBApplication dbApplication = applicationService.getApplicationByOpeningIdAndCandidateId(openingId, candidateId);
        if (dbApplication != null) {
            return ResponseEntity.ok(applicationMapper.entityToModel(dbApplication));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/openings/{openingId}/applications")
    public ResponseEntity<Application> createApplication(@PathVariable Long openingId, @RequestBody DBApplication application) {
//        getpermssion.includes("create user")
        // Permission passed
        // Create appl received log
        // Fetch the corresponding opening from the OpeningService using openingId
        DBOpening opening = openingService.getOpeningById(openingId);
        // Opening
        if (opening != null) {
            // Associate the application with the opening
            application.setOpening(opening);
            application.setStatus(ApplicationStatus.APPLIED);
            application.setAppliedAt(new Date());
            application.setInterviews(new ArrayList<>());
            // Save the application to the database
            DBApplication dbCreatedApplication = applicationService.createApplication(application);
            return ResponseEntity.status(HttpStatus.CREATED).body(applicationMapper.entityToModel(dbCreatedApplication));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<DBApplication> updateApplication(@PathVariable Long id, @RequestBody DBApplication application) {
        DBApplication dbUpdatedApplication = applicationService.updateApplication(id, application);
        return ResponseEntity.ok(dbUpdatedApplication);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable Long id) {
        applicationService.deleteApplication(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/applications/{applicationId}/status")
    public ResponseEntity<Application> updateInterviewStatus(@PathVariable Long applicationId, @RequestParam ApplicationStatus newStatus) {
        DBApplication application = applicationService.getApplicationById(applicationId);
        if (application != null) {
            if (newStatus == ApplicationStatus.APPLIED) {
                return ResponseEntity.badRequest().build();
            } else if (newStatus == ApplicationStatus.REJECTED && application.getStatus() != ApplicationStatus.APPLIED) {
                return ResponseEntity.badRequest().build();
            } else if (newStatus == ApplicationStatus.ALLOCATED && application.getStatus() != ApplicationStatus.APPLIED) {
                return ResponseEntity.badRequest().build();
            }
            application.setStatus(newStatus);
            DBApplication dbUpdatedApplication = applicationService.updateApplication(applicationId, application);
            projectService.allocateUser(dbUpdatedApplication.getOpening().getProject(), dbUpdatedApplication.getCandidate());
            return ResponseEntity.ok(applicationMapper.entityToModel(dbUpdatedApplication));
        }
        return ResponseEntity.notFound().build();
    }
}

