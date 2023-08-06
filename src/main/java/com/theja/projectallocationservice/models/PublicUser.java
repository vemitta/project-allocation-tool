package com.theja.projectallocationservice.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublicUser {
    private Long id;
    private String name;
    private String email;
    private List<Skill> skills;
}
