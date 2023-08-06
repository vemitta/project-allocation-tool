package com.theja.projectallocationservice.services;

import com.theja.projectallocationservice.models.DBUser;
import com.theja.projectallocationservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Page<DBUser> getFreePoolUsers(Integer pageSize, Integer pageNumber) {
        if (pageSize == null) pageSize = 1000;
        if (pageNumber == null) pageNumber = 0;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return userRepository.getFreePoolUsers(pageable);
    }

    public List<DBUser> getAllAllocatedUsers(Date startDate, Date endDate) {
        return userRepository.getAllAllocatedUsers(startDate, endDate);
    }
}
