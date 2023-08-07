package com.theja.projectallocationservice.services;

import com.theja.projectallocationservice.models.DBOpening;
import com.theja.projectallocationservice.models.DBProject;
import com.theja.projectallocationservice.models.DBUser;
import com.theja.projectallocationservice.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public Page<DBProject> getAllProjects(Integer pageSize, Integer pageNumber) {
        if (pageSize == null) pageSize = 1000;
        if (pageNumber == null) pageNumber = 0;
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return projectRepository.findAll(pageRequest);
    }

    public DBProject getProjectById(Long id) {
        Optional<DBProject> project = projectRepository.findById(id);
        return project.orElse(null);
    }

    public List<DBProject> getProjectsForUser(Long userId) {
        // Implement the logic to fetch projects associated with the user
        return projectRepository.getProjectsForUser(userId);
    }

    public DBProject createProject(DBProject project) {
        return projectRepository.save(project);
    }

    public DBProject updateProject(Long id, DBProject updatedProjectData) {
        Optional<DBProject> existingProjectOpt = projectRepository.findById(id);
        if (existingProjectOpt.isPresent()) {
            DBProject existingProject = existingProjectOpt.get();
            // Update the basic properties of the project
            existingProject.setTitle(updatedProjectData.getTitle());
            existingProject.setDetails(updatedProjectData.getDetails());

            // Check if the updatedProjectData has openings and update the existing openings
            if (updatedProjectData.getOpenings() != null) {
                // Clear the existing openings and add the new ones
                existingProject.getOpenings().clear();
                existingProject.getOpenings().addAll(updatedProjectData.getOpenings());
            }

            // Save the updated project
            return projectRepository.save(existingProject);
        } else {
            return null;
        }
    }

    public boolean deleteProject(Long id) {
        Optional<DBProject> project = projectRepository.findById(id);
        if (project.isPresent()) {
            projectRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public void allocateUser(DBProject project, DBUser candidate) {
        if (project.getAllocatedUsers() == null) {
            project.setAllocatedUsers(new HashSet<>());
        }
        project.getAllocatedUsers().add(candidate);
        projectRepository.save(project);
    }

    public List<DBProject> getAllProjectsWithoutPagination() {
        return  projectRepository.findAll();
    }
}

