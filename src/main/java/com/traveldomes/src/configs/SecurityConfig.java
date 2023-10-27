package com.traveldomes.src.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// @SuppressWarnings("deprecation")
@Configuration
@EnableMethodSecurity
public class SecurityConfig {
  @Autowired
  UserDetailsService userDetailsService;

  @Autowired
  JwtFilter jwtFilter;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // disable csrf
    http.csrf(csrf -> {
      csrf.disable();
    });

    // session management
    http.sessionManagement(session -> {
      session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    });

    // authorize request
    http.authorizeHttpRequests(auth -> {
      auth.requestMatchers("/**").permitAll()
          .anyRequest().fullyAuthenticated();
    });

    // set authentication provider
    http.authenticationProvider(authenticationProvider());

    // set filter
    http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    // basic auth
    // http.httpBasic();

    return http.build();
  }

  /*
   * digunakan untuk mengautentikasi user yg mau akses request dan/atau login
   */
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
      throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  /*
   * DaoAuthenticationProvider: provider untuk mencari email dan match-kan
   * passwordnya
   */
  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(passwordEncoder());
    authenticationProvider.setUserDetailsService(userDetailsService);
    return authenticationProvider;
  }

}
