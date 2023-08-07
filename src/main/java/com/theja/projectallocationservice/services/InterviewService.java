package com.theja.projectallocationservice.services;

import com.theja.projectallocationservice.exceptions.ResourceNotFoundException;
import com.theja.projectallocationservice.models.DBInterview;
import com.theja.projectallocationservice.repositories.InterviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InterviewService {
    @Autowired
    private InterviewRepository interviewRepository;

    public DBInterview getInterviewById(Long id) {
        return interviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Interview not found"));
    }

    public DBInterview createInterview(DBInterview interview) {
        return interviewRepository.save(interview);
    }

    public DBInterview updateInterview(Long id, DBInterview interview) {
        DBInterview existingInterview = getInterviewById(id);
        // Update the properties of existingInterview with the properties from the provided interview
        existingInterview.setStatus(interview.getStatus());
        existingInterview.setScheduledTime(interview.getScheduledTime());
        existingInterview.setApplication(interview.getApplication());
        return interviewRepository.save(existingInterview);
    }

    public void deleteInterview(Long id) {
        if (interviewRepository.existsById(id)) {
            interviewRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Interview not found");
        }
    }
}

