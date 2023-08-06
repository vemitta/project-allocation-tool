package com.theja.projectallocationservice.models;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class OpeningsListResponse {
    List<Opening> openings;
    Long totalElements;
}
