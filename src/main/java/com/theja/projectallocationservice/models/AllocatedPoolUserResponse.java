package com.theja.projectallocationservice.models;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class AllocatedPoolUserResponse {
        List<PublicUser> allocatedUsers;
        Long totalElements;
}
