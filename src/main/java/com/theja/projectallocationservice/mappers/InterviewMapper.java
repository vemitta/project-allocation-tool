package com.theja.projectallocationservice.mappers;

import com.theja.projectallocationservice.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class InterviewMapper {

    @Autowired
    private UserMapper userMapper;

    public List<Interview> entityToModel(List<DBInterview> dbInterviews) {
        return dbInterviews.stream().map(this::entityToModel).collect(Collectors.toList());
    }

    public Interview entityToModel(DBInterview dbInterview) {
        return new Interview(dbInterview.getId(), dbInterview.getTitle(), userMapper.entityToPublicModel(dbInterview.getInterviewer()), dbInterview.getStatus(), dbInterview.getScheduledTime(), null);
    }
}
