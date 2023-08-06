package com.theja.projectallocationservice.mappers;

import com.theja.projectallocationservice.models.Application;
import com.theja.projectallocationservice.models.DBApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ApplicationMapper {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private InterviewMapper interviewMapper;

    @Autowired
    private OpeningMapper openingMapper;

    public List<Application> entityToModel(List<DBApplication> dbApplications) {
        return dbApplications.stream().map(this::entityToModel).collect(Collectors.toList());
    }

    public Application entityToModel(DBApplication dbApplication) {
        return new Application(dbApplication.getId(), dbApplication.getStatus(), dbApplication.getAppliedAt(), userMapper.entityToPublicModel(dbApplication.getCandidate()), openingMapper.entityToModel(dbApplication.getOpening()), interviewMapper.entityToModel(dbApplication.getInterviews()));
    }
}
