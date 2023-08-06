package com.theja.projectallocationservice.mappers;

import com.theja.projectallocationservice.models.DBSkill;
import com.theja.projectallocationservice.models.Skill;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SkillMapper {

    public List<Skill> entityToModel(List<DBSkill> dbSkills) {
        if (dbSkills == null) {
            return new ArrayList<>();
        }
        return dbSkills.stream().map(this::entityToModel).collect(Collectors.toList());
    }

    public Skill entityToModel(DBSkill dbSkill) {
        return new Skill(dbSkill.getId(), dbSkill.getTitle(), null, null);
    }
}