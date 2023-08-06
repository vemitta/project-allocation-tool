package com.theja.projectallocationservice.controllers;

import com.theja.projectallocationservice.mappers.ProjectMapper;
import com.theja.projectallocationservice.models.*;
import com.theja.projectallocationservice.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProjectMapper projectMapper;

    // Get all projects
    @GetMapping
    public ResponseEntity<ProjectListResponse> getAllProjects(@RequestParam(required = false) Integer pageSize, @RequestParam(required = false) Integer pageNumber) {
        Page<DBProject> dbProjects = projectService.getAllProjects(pageSize, pageNumber);
        ProjectListResponse response = ProjectListResponse.builder()
                .projects(projectMapper.entityToModel(dbProjects.getContent()))
                .totalElements(dbProjects.getTotalElements())
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<Project>> getProjectsForUser(@PathVariable Long userId) {
        List<DBProject> dbProjects = projectService.getProjectsForUser(userId);
        return ResponseEntity.ok(projectMapper.entityToModel(dbProjects));
    }

    // Get a specific project by ID
    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable("id") Long id) {
        DBProject dbProject = projectService.getProjectById(id);
        if (dbProject != null) {
            return ResponseEntity.ok(projectMapper.entityToModel(dbProject));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Create a new project
    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody DBProject dbProject) {
        DBProject dbCreatedProject = projectService.createProject(dbProject);
        return ResponseEntity.status(HttpStatus.CREATED).body(projectMapper.entityToModel(dbCreatedProject));
    }

    // Update an existing project
    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable("id") Long id, @RequestBody DBProject dbProject) {
        DBProject dbUpdatedProject = projectService.updateProject(id, dbProject);
        if (dbUpdatedProject != null) {
            return ResponseEntity.ok(projectMapper.entityToModel(dbUpdatedProject));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a project
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable("id") Long id) {
        boolean deleted = projectService.deleteProject(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

