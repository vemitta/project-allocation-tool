package com.theja.projectallocationservice.controllers;

import com.theja.projectallocationservice.mappers.SkillMapper;
import com.theja.projectallocationservice.models.*;
import com.theja.projectallocationservice.services.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class SkillController {
    @Autowired
    private SkillService skillService;
    @Autowired
    private SkillMapper skillMapper;

    @GetMapping("/skills")
    public ResponseEntity<List<Skill>> getAllSkills() {
        List<DBSkill> dbSkills = skillService.getAllSkills();
        return ResponseEntity.ok(skillMapper.entityToModel(dbSkills));
    }

    @PostMapping("/skills")
    public ResponseEntity<Skill> addSkill(@RequestBody DBSkill dbSkill) {
        DBSkill createdSkill = skillService.createSkill(dbSkill);
        return ResponseEntity.ok(skillMapper.entityToModel(createdSkill));
    }
}