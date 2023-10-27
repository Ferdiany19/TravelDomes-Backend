package com.traveldomes.src.services.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import com.traveldomes.src.models.Role;
import com.traveldomes.src.models.User;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {
  private Collection<? extends GrantedAuthority> authorities;
  private String password;
  private String email;
  private Boolean isDeleted;

  public static UserDetails buid(User user) {
    Set<Role> roles = user.getRoles();
    Set<String> strRoles = new HashSet<>();
    roles.forEach(role -> {
      strRoles.add(role.getName());
    });

    List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(strRoles);
    return new UserDetailsImpl(authorities, user.getPassword(), user.getEmail(), user.getIsDeleted());
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.authorities;
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public String getUsername() {
    return this.email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return !this.isDeleted;
  }

}
