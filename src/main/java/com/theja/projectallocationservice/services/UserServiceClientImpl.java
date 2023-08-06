package com.theja.projectallocationservice.services;

import com.theja.projectallocationservice.models.PermissionName;
import com.theja.projectallocationservice.models.PublicUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class UserServiceClientImpl implements UserServiceClient {
    @Override
    public List<PermissionName> getPermissions(String authHeader) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<Object> permisssions = new RestTemplate().exchange("http://localhost:9092/api/v1/authorization/permissions", HttpMethod.GET, entity, Object.class);
        List<PermissionName> permissionNames = (List<PermissionName>) permisssions.getBody();
        return permissionNames;
    }

    @Override
    public PublicUser getUser(String authHeader) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<PublicUser> user = new RestTemplate().exchange("http://localhost:9092/api/v1/authorization/user", HttpMethod.GET, entity, PublicUser.class);
        return user.getBody();
    }
}
