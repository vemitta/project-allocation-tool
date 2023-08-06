package com.theja.projectallocationservice.mappers;

import com.theja.projectallocationservice.models.DBUser;
import com.theja.projectallocationservice.models.PublicUser;
import com.theja.projectallocationservice.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    @Autowired
    private SkillMapper skillMapper;

    public Collection<User> entityToModel(Collection<DBUser> dbUsers) {
        if (dbUsers == null) {
            return new ArrayList<>();
        }
        return dbUsers.stream().map(this::entityToModel).collect(Collectors.toList());
    }

    public User entityToModel(DBUser dbUser) {
        return new User(dbUser.getId(), dbUser.getName(), dbUser.getPassword(), dbUser.getEmail(), skillMapper.entityToModel(dbUser.getSkills()), null, null, null, null);
    }

    public PublicUser entityToPublicModel(DBUser dbUser) {
        if (dbUser != null) {
            return new PublicUser(dbUser.getId(), dbUser.getName(), dbUser.getEmail(), skillMapper.entityToModel(dbUser.getSkills()));
        } else {
            return null;
        }
    }

    public List<PublicUser> entityToPublicModel(List<DBUser> dbUsers) {
        return dbUsers.stream().map(this::entityToPublicModel).collect(Collectors.toList());
    }
}
