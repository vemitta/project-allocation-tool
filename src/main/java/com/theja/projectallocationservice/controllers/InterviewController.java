package com.theja.projectallocationservice.controllers;

import com.theja.projectallocationservice.mappers.InterviewMapper;
import com.theja.projectallocationservice.models.*;
import com.theja.projectallocationservice.services.ApplicationService;
import com.theja.projectallocationservice.services.InterviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class InterviewController {

    @Autowired
    private InterviewService interviewService;
    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private InterviewMapper interviewMapper;

    @GetMapping("/interviews/{interviewId}")
    public ResponseEntity<Interview> getInterview(@PathVariable Long interviewId) {
        DBInterview dbInterview = interviewService.getInterviewById(interviewId);
        return ResponseEntity.ok(interviewMapper.entityToModel(dbInterview));
    }

    @PostMapping("/applications/{applicationId}/interviews")
    public ResponseEntity<Interview> createInterview(@PathVariable Long applicationId, @RequestBody DBInterview dbInterview) {
        DBApplication application = applicationService.getApplicationById(applicationId);
        if (application != null) {
            dbInterview.setApplication(application);
            dbInterview.setStatus(InterviewStatus.SCHEDULED);
            DBInterview dbCreatedInterview = interviewService.createInterview(dbInterview);
            return ResponseEntity.status(HttpStatus.CREATED).body(interviewMapper.entityToModel(dbCreatedInterview));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/interviews/{interviewId}")
    public ResponseEntity<Interview> updateInterview(@PathVariable Long interviewId, @RequestBody DBInterview dbInterview) {
        DBInterview dbUpdatedInterview = interviewService.updateInterview(interviewId, dbInterview);
        return ResponseEntity.ok(interviewMapper.entityToModel(dbUpdatedInterview));
    }

    @PatchMapping("/interviews/{interviewId}/status")
    public ResponseEntity<Interview> updateInterviewStatus(@PathVariable Long interviewId, @RequestParam InterviewStatus newStatus) {
        DBInterview interview = interviewService.getInterviewById(interviewId);
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
            return ResponseEntity.ok(interviewMapper.entityToModel(dbUpdatedInterview));
        }
        return ResponseEntity.notFound().build();
    }
}

