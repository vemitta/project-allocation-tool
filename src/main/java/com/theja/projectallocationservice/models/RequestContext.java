package com.theja.projectallocationservice.models;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;

@RequestScope
@Component
@Data
public class RequestContext {
    List<PermissionName> permissions;

    PublicUser loggedinUser;
}
