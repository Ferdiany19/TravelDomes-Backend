package com.traveldomes.src.services.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.traveldomes.src.exceptions.EntityFoundException;
import com.traveldomes.src.models.User;
import com.traveldomes.src.payloads.req.RegisterRequest;
import com.traveldomes.src.payloads.res.ResponseHandler;
import com.traveldomes.src.repositories.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<?> registerUserService(RegisterRequest request) {
        // cek apakah email sudah terdaftar atau belum
        User users = userRepository.findByEmail(request.getEmail());
        if (users != null) {
            throw new EntityNotFoundException("Email already Exists");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new EntityFoundException("Username already exists!");
        }

        User user = new User(request.getUsername(), request.getFullname(), request.getEmail(),
                passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        return ResponseHandler.responseData(HttpStatus.CREATED.value(), "User Successfully Registered!", user);
    }

}
