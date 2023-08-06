package com.theja.projectallocationservice.mappers;

import com.theja.projectallocationservice.models.DBOpening;
import com.theja.projectallocationservice.models.Opening;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OpeningMapper {
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private SkillMapper skillMapper;
    @Autowired
    private UserMapper userMapper;

    public List<Opening> entityToModel(List<DBOpening> dbOpenings) {
        return dbOpenings.stream().map(this::entityToModel).collect(Collectors.toList());
    }

    public Opening entityToModel(DBOpening dbOpening) {
        return new Opening(dbOpening.getId(), dbOpening.getTitle(), dbOpening.getDetails(), dbOpening.getLevel(), dbOpening.getLocation(), dbOpening.getStatus(), userMapper.entityToPublicModel(dbOpening.getRecruiter()), projectMapper.entityToModel(dbOpening.getProject()), null, skillMapper.entityToModel(dbOpening.getSkills()));
    }
}
