package com.traveldomes.src.services.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.traveldomes.src.models.User;
import com.traveldomes.src.repositories.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired
  UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    if (!userRepository.existsByEmail(username)) {
      throw new UsernameNotFoundException(username + " is not found!");
    }
    User user = userRepository.findByEmail(username);
    return UserDetailsImpl.buid(user);
  }

}
