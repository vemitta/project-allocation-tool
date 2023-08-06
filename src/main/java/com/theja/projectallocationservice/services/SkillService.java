package com.theja.projectallocationservice.services;

import com.theja.projectallocationservice.models.DBOpening;
import com.theja.projectallocationservice.models.DBProject;
import com.theja.projectallocationservice.models.DBSkill;
import com.theja.projectallocationservice.repositories.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SkillService {

    @Autowired
    private SkillRepository skillRepository;

    public Optional<DBSkill> getSkillById(Long skillId) {
        return skillRepository.findById(skillId);
    }

    public DBSkill createSkill(DBSkill skill) {
        return skillRepository.save(skill);
    }

    public List<DBSkill> getAllSkills() {
        return skillRepository.findAll();
    }
}
