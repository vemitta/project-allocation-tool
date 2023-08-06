package com.theja.projectallocationservice.repositories;

import com.theja.projectallocationservice.models.DBProject;
import com.theja.projectallocationservice.models.DBUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<DBProject, Long> {

    // You can define additional custom query methods here if needed
    @Query(value = "SELECT p.* FROM projects p JOIN users_projects up ON p.id = up.project_id WHERE up.user_id = :userId", nativeQuery = true)
    List<DBProject> getProjectsForUser(Long userId);
}

