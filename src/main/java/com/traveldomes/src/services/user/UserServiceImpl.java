package com.traveldomes.src.services.user;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.traveldomes.src.configs.JwtUtil;
import com.traveldomes.src.exceptions.EntityFoundException;
import com.traveldomes.src.models.Role;
import com.traveldomes.src.models.User;
import com.traveldomes.src.payloads.req.LoginRequest;
import com.traveldomes.src.payloads.req.RegisterRequest;
import com.traveldomes.src.payloads.res.ResponseHandler;
import com.traveldomes.src.repositories.RoleRepository;
import com.traveldomes.src.repositories.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtil jwtUtil;

    @Override
    public ResponseEntity<?> registerUserService(RegisterRequest request, String role) {
        // cek apakah email sudah terdaftar atau belum
        User users = userRepository.findByEmail(request.getEmail());
        if (users != null) {
            throw new EntityNotFoundException("Email already Exists");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new EntityFoundException("Username already exists!");
        }

        String strRole = (role.equals("") || Objects.isNull(role) || role.equals(null)) ? "ROLE_USER" : role;
        Role roleUser = roleRepository.findByName(strRole);

        if (Objects.isNull(roleUser)) {
            roleUser = new Role(strRole);
            roleRepository.save(roleUser);
        }
        Set<Role> roles = new HashSet<>();
        roles.add(roleUser);

        User user = new User(request.getUsername(), request.getFullname(), request.getEmail(),
                passwordEncoder.encode(request.getPassword()));
        user.setRoles(roles);
        userRepository.save(user);

        return ResponseHandler.responseData(HttpStatus.CREATED.value(), "User Successfully Registered!", user);
    }

    @Override
    public ResponseEntity<?> loginUserService(LoginRequest request) {

        if (!userRepository.existsByEmail(request.getEmail())) {
            throw new EntityNotFoundException("Email not found!");
        }

        User user = userRepository.findByEmail(request.getEmail());

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                request.getEmail(), request.getPassword());
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtUtil.createToken(request.getEmail());

        Map<String, Object> data = new HashMap<>();
        data.put("email", request.getEmail());
        data.put("token", token);
        data.put("roles", user.getRoles());
        data.put("username", user.getUsername());
        data.put("userId", user.getUserId());

        return ResponseHandler.responseData(HttpStatus.OK.value(), "Succesfully logged in!", data);
    }

}
