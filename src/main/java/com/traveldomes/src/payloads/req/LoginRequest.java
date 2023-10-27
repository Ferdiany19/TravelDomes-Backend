package com.traveldomes.src.payloads.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginRequest {
    @NotEmpty(message = "Email cannot be empty!")
    @Email
    private String email;

    @NotEmpty(message = "Password cannot be empty!")
    private String password;
}
