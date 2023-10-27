package com.traveldomes.src.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.traveldomes.src.models.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
  Role findByName(String name);
}
