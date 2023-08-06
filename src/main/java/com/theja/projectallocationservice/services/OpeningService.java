package com.theja.projectallocationservice.services;

import com.theja.projectallocationservice.models.DBOpening;
import com.theja.projectallocationservice.models.RequestContext;
import com.theja.projectallocationservice.repositories.OpeningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OpeningService {

    @Autowired
    private OpeningRepository openingRepository;

    @Autowired
    private RequestContext requestContext;

//    public Page<DBOpening> getAllOpenings(Integer pageSize, Integer pageNumber) {
//        if (pageSize == null) pageSize = 1000;
//        if (pageNumber == null) pageNumber = 0;
//        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
//        return openingRepository.findAll(pageRequest);
//    }

//    public List<DBOpening> getAllOpenings() {
//        return openingRepository.findAll();
//    }


    public Page<DBOpening> getAllOpenings(Integer pageSize, Integer pageNumber, Boolean appliedBySelf, Boolean postedBySelf) {
        if (pageSize == null) pageSize = 1000;
        if (pageNumber == null) pageNumber = 0;
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return openingRepository.fetchOpenings(appliedBySelf, postedBySelf, pageRequest, requestContext.getLoggedinUser().getId());
    }

    public List<DBOpening> getAllOpeningsForProject(Long projectId) {
        return openingRepository.findByProjectId(projectId);
    }

    public DBOpening getOpeningById(Long id) {
        Optional<DBOpening> opening = openingRepository.findById(id);
        return opening.orElse(null);
    }

    public DBOpening createOpening(DBOpening opening) {
        return openingRepository.save(opening);
    }

    public DBOpening updateOpening(Long id, DBOpening opening) {
        Optional<DBOpening> existingOpening = openingRepository.findById(id);
        if (existingOpening.isPresent()) {
            opening.setId(id);
            return openingRepository.save(opening);
        } else {
            return null;
        }
    }

    public boolean deleteOpening(Long id) {
        Optional<DBOpening> opening = openingRepository.findById(id);
        if (opening.isPresent()) {
            openingRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}

