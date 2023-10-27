package com.traveldomes.src.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.traveldomes.src.payloads.req.RegisterRequest;
import com.traveldomes.src.services.user.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid RegisterRequest request,
            @RequestParam(value = "role", defaultValue = "") String role) {
        return userService.registerUserService(request, role);
    }
}
