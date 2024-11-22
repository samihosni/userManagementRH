package com.example.userManagementRH.authentication;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class AuthenticationResponse {
    private String token;
    private Long userId;
    private String role;
    private String fullName;
}
