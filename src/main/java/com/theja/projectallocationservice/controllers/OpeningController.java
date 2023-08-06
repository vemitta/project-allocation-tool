package com.theja.projectallocationservice.controllers;

import com.theja.projectallocationservice.mappers.OpeningMapper;
import com.theja.projectallocationservice.models.*;
import com.theja.projectallocationservice.services.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class OpeningController {
    @Autowired
    private OpeningService openingService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private OpeningMapper openingMapper;
    @Autowired
    private SkillService skillService;
    @Autowired
    private AuditLogService auditLogService;
    @Autowired
    private RequestContext requestContext;
    @Autowired
    private AuditCommentService auditCommentService;

    // Get all openings
    @GetMapping("/openings")
    public ResponseEntity<OpeningsListResponse> getAllOpenings(@RequestParam(required = false) Integer pageSize, @RequestParam(required = false) Integer pageNumber, @RequestParam(required = false) Boolean appliedBySelf, @RequestParam(required = false) Boolean postedBySelf) {
        Page<DBOpening> dbOpenings = openingService.getAllOpenings(pageSize, pageNumber, appliedBySelf, postedBySelf);
        OpeningsListResponse response = OpeningsListResponse.builder()
                .openings(openingMapper.entityToModel(dbOpenings.getContent()))
                .totalElements(dbOpenings.getTotalElements())
                .build();
        return ResponseEntity.ok(response);
    }

//    @GetMapping("/openings")
//    public ResponseEntity<List<Opening>> getAllOpenings() {
//        List<DBOpening> dbOpenings = openingService.getAllOpenings();
//        return ResponseEntity.ok(openingMapper.entityToModel(dbOpenings));
//    }

    // Get openings for a specific project
    @GetMapping("/projects/{projectId}/openings")
    public ResponseEntity<List<Opening>> getAllOpeningsForProject(@PathVariable Long projectId) {
        List<DBOpening> dbOpenings = openingService.getAllOpeningsForProject(projectId);
        return ResponseEntity.ok(openingMapper.entityToModel(dbOpenings));
    }

    // Get a specific opening by ID
    @GetMapping("/openings/{id}")
    public ResponseEntity<Opening> getOpeningById(@PathVariable("id") Long id) {
        DBOpening dbOpening = openingService.getOpeningById(id);
        if (dbOpening != null) {
            return ResponseEntity.ok(openingMapper.entityToModel(dbOpening));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

//    // Create a new opening
//    @PostMapping("/projects/{projectId}/openings")
//    public ResponseEntity<Opening> createOpening(@RequestBody DBOpening dbOpening, @PathVariable Long projectId) {
//        dbOpening.setProject(projectService.getProjectById(projectId));
//        DBOpening dbCreatedOpening = openingService.createOpening(dbOpening);
//        return ResponseEntity.status(HttpStatus.CREATED).body(openingMapper.entityToModel(dbCreatedOpening));
//    }


    @PostMapping("/projects/{projectId}/openings")
    public ResponseEntity<Opening> createOpening(@RequestBody @Validated DBOpening dbOpening, @PathVariable Long projectId) {
        // Create Audit log
        DBAuditLog auditLog = auditLogService.createAuditLog(
                DBAuditLog.builder()
                        .action("Create Opening for project " + projectId)
                        .user(DBUser.builder().id(requestContext.getLoggedinUser().getId()).build())
                        .loggedAt(new Date())
                        .auditComments(new ArrayList<>())
                        .build());
        auditCommentService.createAuditComment(DBAuditComment.builder()
                        .comment("Checking user permissions")
                        .auditLog(auditLog)
                .build());
        if (requestContext.getPermissions() == null || !requestContext.getPermissions().contains(PermissionName.CREATE_OPENING.toString())) {
            auditCommentService.createAuditComment(DBAuditComment.builder()
                    .comment("Unauthorized user trying to create project")
                    .auditLog(auditLog)
                    .build());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        auditCommentService.createAuditComment(DBAuditComment.builder()
                .comment("Permissions passed")
                .auditLog(auditLog)
                .build());
        dbOpening.setProject(projectService.getProjectById(projectId));
        auditCommentService.createAuditComment(DBAuditComment.builder()
                .comment("Project mapped to opening")
                .auditLog(auditLog)
                .build());
        // Retrieve the Skill entities from the database using the provided skillIds
        List<DBSkill> skills = new ArrayList<>();
        for (DBSkill skill : dbOpening.getSkills()) {
            skills.add(skillService.getSkillById(skill.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Skill not found with ID: " + skill.getId())));
        }
        dbOpening.setSkills(skills);
        auditCommentService.createAuditComment(DBAuditComment.builder()
                .comment("Skills assigned to opening")
                .auditLog(auditLog)
                .build());
        dbOpening.setStatus(OpeningStatus.ACTIVE);
        DBOpening dbCreatedOpening = openingService.createOpening(dbOpening);
        Opening createdOpening = openingMapper.entityToModel(dbCreatedOpening);
        auditCommentService.createAuditComment(DBAuditComment.builder()
                .comment("Opening created")
                .auditLog(auditLog)
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOpening);
    }


    // Update an existing opening
//    @PutMapping("/projects/{projectId}/openings/{id}")
//    public ResponseEntity<Opening> updateOpening(@PathVariable("id") Long id, @RequestBody DBOpening dbOpening) {
//        DBOpening dbUpdatedOpening = openingService.updateOpening(id, dbOpening);
//        if (dbUpdatedOpening != null) {
//            return ResponseEntity.ok(openingMapper.entityToModel(dbUpdatedOpening));
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }

    @PutMapping("/openings/{id}")
    public ResponseEntity<Opening> updateOpening(
            @PathVariable("id") Long id,
            @RequestBody DBOpening dbOpening) {

        DBOpening existingOpening = openingService.getOpeningById(id);
        if (existingOpening == null) {
            return ResponseEntity.notFound().build();
        }

        // Update the properties of the existingOpening with the new values from the request payload
        existingOpening.setTitle(dbOpening.getTitle());
        existingOpening.setDetails(dbOpening.getDetails());
        existingOpening.setLevel(dbOpening.getLevel());
        existingOpening.setLocation(dbOpening.getLocation());
        existingOpening.setStatus(dbOpening.getStatus());
        existingOpening.setSkills(dbOpening.getSkills());

        // Save the updated opening
        DBOpening updatedOpening = openingService.updateOpening(id, existingOpening);
        if (updatedOpening == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        // Return the updated opening in the response
        Opening responseOpening = openingMapper.entityToModel(updatedOpening);
        return ResponseEntity.ok(responseOpening);
    }


    // Delete an opening
    @DeleteMapping("/openings/{id}")
    public ResponseEntity<Void> deleteOpening(@PathVariable("id") Long id) {
        boolean deleted = openingService.deleteOpening(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
