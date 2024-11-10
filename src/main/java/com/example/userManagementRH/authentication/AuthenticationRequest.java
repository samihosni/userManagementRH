package com.example.userManagementRH.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
    @Email(message ="Email is no formatted")
    @NotEmpty(message = "Email is mandatory")
    @NotBlank(message="Email is mandatory")
    private String email;
    @NotEmpty(message = "Password is mandatory")
    @NotBlank(message = "Password is mandatory")
    @Size(min=5, message = "Password should be 5 characters long minimum")
    private String password;

}
