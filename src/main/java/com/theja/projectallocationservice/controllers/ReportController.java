package com.theja.projectallocationservice.controllers;

import com.theja.projectallocationservice.mappers.UserMapper;
import com.theja.projectallocationservice.models.*;
import com.theja.projectallocationservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ReportController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

//    @GetMapping("/reports/users/free")
//    public ResponseEntity<List<PublicUser>> getAllFreePoolUsers() {
//        List<DBUser> dbUsers = userService.getFreePoolUsers();
//
//
//        return ResponseEntity.ok(userMapper.entityToPublicModel(dbUsers));
//    }

    @GetMapping("/reports/users/free")
    public ResponseEntity<FreePoolUserResponse> getAllFreePoolUsers(@RequestParam(required = false) Integer pageSize, @RequestParam(required = false) Integer pageNumber) {
        Page<DBUser> dbUsers = userService.getFreePoolUsers(pageSize, pageNumber);
        FreePoolUserResponse response = FreePoolUserResponse.builder()
                .freePoolUsers(userMapper.entityToPublicModel(dbUsers.getContent()))
                .totalElements(dbUsers.getTotalElements())
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/reports/users/allocated")
    public ResponseEntity<List<PublicUser>> getAllAllocatedUsers(@RequestParam String startDate, @RequestParam String endDate) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        List<DBUser> dbUsers = userService.getAllAllocatedUsers(formatter.parse(startDate), formatter.parse(endDate));
        return ResponseEntity.ok(userMapper.entityToPublicModel(dbUsers));
    }
}
