package com.theja.projectallocationservice.services;

import com.theja.projectallocationservice.models.PermissionName;
import com.theja.projectallocationservice.models.PublicUser;
import org.springframework.stereotype.Component;

import java.util.List;

public interface UserServiceClient {
    List<PermissionName> getPermissions(String userToken);

    PublicUser getUser(String authHeader);
}
