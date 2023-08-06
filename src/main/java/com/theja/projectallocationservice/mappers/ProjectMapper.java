package com.theja.projectallocationservice.mappers;

import com.theja.projectallocationservice.models.DBProject;
import com.theja.projectallocationservice.models.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProjectMapper {

    @Autowired
    private UserMapper userMapper;

    public List<Project> entityToModel(List<DBProject> dbProjects) {
        return dbProjects.stream().map(this::entityToModel).collect(Collectors.toList());
    }

    public Project entityToModel(DBProject dbProject) {
        return new Project(dbProject.getId(), dbProject.getTitle(), dbProject.getDetails(), userMapper.entityToModel(dbProject.getAllocatedUsers()),null);
    }
}
