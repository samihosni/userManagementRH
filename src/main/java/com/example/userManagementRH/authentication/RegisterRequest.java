package com.example.userManagementRH.authentication;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegisterRequest {
    private Long id;
    @NotEmpty(message = "Firstname is mandatory")
    @NotBlank(message = "Firstname is mandatory")
    private String firstname;
    @NotEmpty(message = "Lastname is mandatory")
    @NotBlank(message = "Lastname is mandatory")
    private String lastname;
    @Email(message = "Not a valid format !")
    @NotEmpty(message = "Email is mandatory")
    @NotBlank(message = "Email is mandatory")
    private String email;
    @NotEmpty(message = "Fill the password space !")
    @NotBlank(message = "Fill the password space")
    @Size(min = 5, message = "Password should be at least 5 characters !")
    private String password;
    @NotEmpty(message = "Role is mandatory")
    @NotBlank(message = "Role is mandatory")
    private String role;

    @NotEmpty(message = "Phone is mandatory")
    @NotBlank(message = "Phone is mandatory")
    private String phone;


    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

}
