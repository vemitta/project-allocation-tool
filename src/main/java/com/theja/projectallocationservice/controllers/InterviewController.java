package com.theja.projectallocationservice.controllers;

import com.theja.projectallocationservice.exceptions.ResourceNotFoundException;
import com.theja.projectallocationservice.exceptions.UnauthorizedAccessException;
import com.theja.projectallocationservice.mappers.InterviewMapper;
import com.theja.projectallocationservice.models.*;
import com.theja.projectallocationservice.services.ApplicationService;
import com.theja.projectallocationservice.services.AuditCommentService;
import com.theja.projectallocationservice.services.AuditLogService;
import com.theja.projectallocationservice.services.InterviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;

@RestController
@RequestMapping("/api/v1")
public class InterviewController {
    @Autowired
    private InterviewService interviewService;
    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private InterviewMapper interviewMapper;
    @Autowired
    private RequestContext requestContext;
    @Autowired
    private AuditLogService auditLogService;
    @Autowired
    private AuditCommentService auditCommentService;

    @GetMapping("/interviews/{interviewId}")
    public ResponseEntity<Interview> getInterview(@PathVariable Long interviewId) {
        DBInterview dbInterview = interviewService.getInterviewById(interviewId);
        return ResponseEntity.ok(interviewMapper.entityToModel(dbInterview));
    }

    @PostMapping("/applications/{applicationId}/interviews")
    public ResponseEntity<Interview> createInterview(@PathVariable Long applicationId, @RequestBody DBInterview dbInterview) {
        // Create Audit log
        DBAuditLog auditLog = auditLogService.createAuditLog(
                DBAuditLog.builder()
                        .action("Scheduling interview for application id " + applicationId)
                        .user(DBUser.builder().id(requestContext.getLoggedinUser().getId()).build())
                        .loggedAt(new Date())
                        .auditComments(new ArrayList<>())
                        .build());
        auditCommentService.createAuditComment(DBAuditComment.builder()
                .comment("Checking user permissions")
                .auditLog(auditLog)
                .build());
        if (requestContext.getPermissions() == null || !requestContext.getPermissions().contains(PermissionName.VIEW_PENDING_APPLICATIONS.toString())) {
            throw new UnauthorizedAccessException("You don't have permission to schedule an interview.");
        }
        DBApplication application = applicationService.getApplicationById(applicationId);
        auditCommentService.createAuditComment(DBAuditComment.builder()
                .comment("Application with id " + applicationId + " found")
                .auditLog(auditLog)
                .build());
        if (application != null) {
            dbInterview.setApplication(application);
            dbInterview.setStatus(InterviewStatus.SCHEDULED);
            DBInterview dbCreatedInterview = interviewService.createInterview(dbInterview);
            auditCommentService.createAuditComment(DBAuditComment.builder()
                    .comment("Interview created successfully")
                    .auditLog(auditLog)
                    .build());
            return ResponseEntity.status(HttpStatus.CREATED).body(interviewMapper.entityToModel(dbCreatedInterview));
        } else {
            throw new ResourceNotFoundException("Application not found with ID: " + applicationId);
        }
    }

    @PutMapping("/interviews/{interviewId}")
    public ResponseEntity<Interview> updateInterview(@PathVariable Long interviewId, @RequestBody DBInterview dbInterview) {
        if (requestContext.getPermissions() == null || !requestContext.getPermissions().contains(PermissionName.VIEW_PENDING_APPLICATIONS.toString())) {
            throw new UnauthorizedAccessException("You don't have permission to update the interview details.");
        }
        DBInterview dbUpdatedInterview = interviewService.updateInterview(interviewId, dbInterview);
        return ResponseEntity.ok(interviewMapper.entityToModel(dbUpdatedInterview));
    }

    @PatchMapping("/interviews/{interviewId}/status")
    public ResponseEntity<Interview> updateInterviewStatus(@PathVariable Long interviewId, @RequestParam InterviewStatus newStatus) {
        // Create Audit log
        DBAuditLog auditLog = auditLogService.createAuditLog(
                DBAuditLog.builder()
                        .action("Updating status of interview with id " + interviewId)
                        .user(DBUser.builder().id(requestContext.getLoggedinUser().getId()).build())
                        .loggedAt(new Date())
                        .auditComments(new ArrayList<>())
                        .build());
        auditCommentService.createAuditComment(DBAuditComment.builder()
                .comment("Checking user permissions")
                .auditLog(auditLog)
                .build());
        if (requestContext.getPermissions() == null || !requestContext.getPermissions().contains(PermissionName.VIEW_PENDING_APPLICATIONS.toString())) {
            throw new UnauthorizedAccessException("You don't have permission to update the interview status.");
        }
        DBInterview interview = interviewService.getInterviewById(interviewId);
        auditCommentService.createAuditComment(DBAuditComment.builder()
                .comment("Interview with id " + interviewId + " found")
                .auditLog(auditLog)
                .build());
        if (interview != null) {
            if (newStatus == InterviewStatus.SCHEDULED) {
                return ResponseEntity.badRequest().build();
            } else if (newStatus == InterviewStatus.COMPLETED && interview.getStatus() != InterviewStatus.SCHEDULED) {
                return ResponseEntity.badRequest().build();
            } else if (newStatus == InterviewStatus.CANCELLED && interview.getStatus() != InterviewStatus.SCHEDULED) {
                return ResponseEntity.badRequest().build();
            }
            interview.setStatus(newStatus);
            DBInterview dbUpdatedInterview = interviewService.updateInterview(interviewId, interview);
            auditCommentService.createAuditComment(DBAuditComment.builder()
                    .comment("Interview status updated successfully")
                    .auditLog(auditLog)
                    .build());
            return ResponseEntity.ok(interviewMapper.entityToModel(dbUpdatedInterview));
        }
        throw new ResourceNotFoundException("Interview not found with ID: " + interviewId);
    }
}
