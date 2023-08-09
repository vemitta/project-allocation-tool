package com.theja.projectallocationservice.controllers;

import com.theja.projectallocationservice.exceptions.ApplicationNotFoundException;
import com.theja.projectallocationservice.exceptions.ResourceNotFoundException;
import com.theja.projectallocationservice.exceptions.UnauthorizedAccessException;
import com.theja.projectallocationservice.mappers.ApplicationMapper;
import com.theja.projectallocationservice.models.*;
import com.theja.projectallocationservice.services.*;
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
    @Autowired
    private AuditLogService auditLogService;
    @Autowired
    private RequestContext requestContext;
    @Autowired
    private AuditCommentService auditCommentService;

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
            throw new ResourceNotFoundException("Application not found with opening ID: " + openingId + " or candidate ID " + candidateId);
        }
    }

    @PostMapping("/openings/{openingId}/applications")
    public ResponseEntity<Application> createApplication(@PathVariable Long openingId, @RequestBody DBApplication application) {
        // Create Audit log
        DBAuditLog auditLog = auditLogService.createAuditLog(
                DBAuditLog.builder()
                        .action("Applying for opening " + openingId)
                        .user(DBUser.builder().id(requestContext.getLoggedinUser().getId()).build())
                        .loggedAt(new Date())
                        .auditComments(new ArrayList<>())
                        .build());
        auditCommentService.createAuditComment(DBAuditComment.builder()
                .comment("Checking user permissions")
                .auditLog(auditLog)
                .build());
        // Fetch the corresponding opening from the OpeningService using openingId
        DBOpening opening = openingService.getOpeningById(openingId);
        auditCommentService.createAuditComment(DBAuditComment.builder()
                .comment("Opening with id " + openingId + " found")
                .auditLog(auditLog)
                .build());
        // Opening
        if (opening != null) {
            // Associate the application with the opening
            application.setOpening(opening);
            application.setStatus(ApplicationStatus.APPLIED);
            application.setAppliedAt(new Date());
            application.setInterviews(new ArrayList<>());
            // Save the application to the database
            DBApplication dbCreatedApplication = applicationService.createApplication(application);
            auditCommentService.createAuditComment(DBAuditComment.builder()
                    .comment("Applied for opening successfully")
                    .auditLog(auditLog)
                    .build());
            return ResponseEntity.status(HttpStatus.CREATED).body(applicationMapper.entityToModel(dbCreatedApplication));
        } else {
            throw new ResourceNotFoundException("Opening not found with ID: " + openingId);
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
        // Create Audit log
        DBAuditLog auditLog = auditLogService.createAuditLog(
                DBAuditLog.builder()
                        .action("Updating status of application id " + applicationId)
                        .user(DBUser.builder().id(requestContext.getLoggedinUser().getId()).build())
                        .loggedAt(new Date())
                        .auditComments(new ArrayList<>())
                        .build());
        auditCommentService.createAuditComment(DBAuditComment.builder()
                .comment("Checking user permissions")
                .auditLog(auditLog)
                .build());
        if (requestContext.getPermissions() == null || !requestContext.getPermissions().contains(PermissionName.VIEW_PENDING_APPLICATIONS.toString())) {
            auditCommentService.createAuditComment(DBAuditComment.builder()
                    .comment("Unauthorized user trying to update application status")
                    .auditLog(auditLog)
                    .build());
            throw new UnauthorizedAccessException("You don't have permission to update the application status.");
        }
        DBApplication application = applicationService.getApplicationById(applicationId);
        auditCommentService.createAuditComment(DBAuditComment.builder()
                .comment("Application with " + applicationId + " found")
                .auditLog(auditLog)
                .build());
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
            auditCommentService.createAuditComment(DBAuditComment.builder()
                    .comment("Updated the application status")
                    .auditLog(auditLog)
                    .build());
            if (newStatus != ApplicationStatus.REJECTED) {
                projectService.allocateUser(dbUpdatedApplication.getOpening().getProject(), dbUpdatedApplication.getCandidate());
                auditCommentService.createAuditComment(DBAuditComment.builder()
                        .comment("Application accepted and applicant is allocated to the project")
                        .auditLog(auditLog)
                        .build());
            } else {
                auditCommentService.createAuditComment(DBAuditComment.builder()
                        .comment("Application rejected")
                        .auditLog(auditLog)
                        .build());
            }
            return ResponseEntity.ok(applicationMapper.entityToModel(dbUpdatedApplication));
        }
        throw new ApplicationNotFoundException(applicationId);
    }
}

