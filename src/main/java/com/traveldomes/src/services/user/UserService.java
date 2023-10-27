package com.traveldomes.src.services.user;

import org.springframework.http.ResponseEntity;

import com.traveldomes.src.payloads.req.RegisterRequest;

public interface UserService {
    ResponseEntity<?> registerUserService(RegisterRequest request, String role);
}
