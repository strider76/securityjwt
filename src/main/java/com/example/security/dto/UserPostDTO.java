package com.example.security.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserPostDTO {

    private String email;
    private String password;
    private List<String> roles;

}
