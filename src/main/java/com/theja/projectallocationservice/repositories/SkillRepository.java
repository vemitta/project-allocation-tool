package com.theja.projectallocationservice.repositories;

import com.theja.projectallocationservice.models.DBSkill;
import com.theja.projectallocationservice.models.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillRepository extends JpaRepository<DBSkill, Long> {
    List<DBSkill> findByUsersId(Long userId);

    List<DBSkill> findByOpeningsId(Long openingId);
}
