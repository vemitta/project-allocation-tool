package com.theja.projectallocationservice.controllers;

import com.theja.projectallocationservice.exceptions.ResourceNotFoundException;
import com.theja.projectallocationservice.exceptions.UnauthorizedAccessException;
import com.theja.projectallocationservice.mappers.ProjectMapper;
import com.theja.projectallocationservice.models.*;
import com.theja.projectallocationservice.services.AuditCommentService;
import com.theja.projectallocationservice.services.AuditLogService;
import com.theja.projectallocationservice.services.ProjectService;
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
public class ProjectController {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private RequestContext requestContext;
    @Autowired
    private AuditLogService auditLogService;
    @Autowired
    private AuditCommentService auditCommentService;

    // Get all projects
    @GetMapping("/projects")
    public ResponseEntity<ProjectListResponse> getAllProjects(@RequestParam(required = false) Integer pageSize, @RequestParam(required = false) Integer pageNumber) {
        Page<DBProject> dbProjects = projectService.getAllProjects(pageSize, pageNumber);
        ProjectListResponse response = ProjectListResponse.builder()
                .projects(projectMapper.entityToModel(dbProjects.getContent()))
                .totalElements(dbProjects.getTotalElements())
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/projects/all")
    public ResponseEntity<List<Project>> getAllProjects() {
        List<DBProject> dbProjects = projectService.getAllProjectsWithoutPagination();
        return ResponseEntity.ok(projectMapper.entityToModel(dbProjects));
    }

    @GetMapping("/projects/users/{userId}")
    public ResponseEntity<List<Project>> getProjectsForUser(@PathVariable Long userId) {
        List<DBProject> dbProjects = projectService.getProjectsForUser(userId);
        return ResponseEntity.ok(projectMapper.entityToModel(dbProjects));
    }

    // Get a specific project by ID
    @GetMapping("/projects/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable("id") Long id) {
        DBProject dbProject = projectService.getProjectById(id);
        if (dbProject != null) {
            return ResponseEntity.ok(projectMapper.entityToModel(dbProject));
        } else {
            throw new ResourceNotFoundException("Project not found with ID: " + id);
        }
    }

    // Create a new project
    @PostMapping("/projects")
    public ResponseEntity<Project> createProject(@RequestBody CreateProjectDTO projectDTO) {
        // Create Audit log
        DBAuditLog auditLog = auditLogService.createAuditLog(
                DBAuditLog.builder()
                        .action("Creating project")
                        .user(DBUser.builder().id(requestContext.getLoggedinUser().getId()).build())
                        .loggedAt(new Date())
                        .auditComments(new ArrayList<>())
                        .build());
        auditCommentService.createAuditComment(DBAuditComment.builder()
                .comment("Checking user permissions")
                .auditLog(auditLog)
                .build());
        if (requestContext.getPermissions() == null || !requestContext.getPermissions().contains(PermissionName.CREATE_PROJECT.toString())) {
            auditCommentService.createAuditComment(DBAuditComment.builder()
                    .comment("Unauthorized user trying to create project")
                    .auditLog(auditLog)
                    .build());
            throw new UnauthorizedAccessException("You don't have permission to create a project.");
        }
        auditCommentService.createAuditComment(DBAuditComment.builder()
                .comment("Permissions passed")
                .auditLog(auditLog)
                .build());
        DBProject dbCreatedProject = projectService.createProject(projectDTO);
        auditCommentService.createAuditComment(DBAuditComment.builder()
                .comment("Project created")
                .auditLog(auditLog)
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).body(projectMapper.entityToModel(dbCreatedProject));
    }

    // Update an existing project
    @PutMapping("/projects/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable("id") Long id, @RequestBody DBProject dbProject) {
        DBProject dbUpdatedProject = projectService.updateProject(id, dbProject);
        if (dbUpdatedProject != null) {
            return ResponseEntity.ok(projectMapper.entityToModel(dbUpdatedProject));
        } else {
            throw new ResourceNotFoundException("Project not found with ID: " + id);
        }
    }

    // Delete a project
    @DeleteMapping("/projects/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable("id") Long id) {
        boolean deleted = projectService.deleteProject(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            throw new ResourceNotFoundException("Project not found with ID: " + id);
        }
    }
}

