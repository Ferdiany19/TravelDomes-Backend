package com.traveldomes.src.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.traveldomes.src.models.User;

public interface UserRepository extends JpaRepository<User, String> {
    User findByEmail(String email);
}
