package com.traveldomes.src.payloads.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotEmpty(message = "Username cannot be empty!")
    private String username;

    @NotEmpty(message = "Fullname cannot be empty")
    private String fullname;

    @NotEmpty(message = "Email cannot be empty!")
    @Email
    private String email;

    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 8, max = 20, message = "Password shoud be minimum 8 and maximum 20 characters")
    private String password;
}
