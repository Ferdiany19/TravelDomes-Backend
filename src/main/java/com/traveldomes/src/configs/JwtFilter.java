package com.traveldomes.src.configs;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.traveldomes.src.services.security.UserDetailsServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*
 * kelas untuk filter jwt, request yang masuk harus dicek token jwtnya
 */
@Component
public class JwtFilter extends OncePerRequestFilter {
  @Autowired
  JwtUtil jwtUtil;

  @Autowired
  UserDetailsServiceImpl userDetailsServiceImpl;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    /*
     * check request yg datang dengan header authorization atau tidak?
     */
    String headerAuth = request.getHeader("Authorization");
    if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
      /*
       * apakah ada tokennya atau tidak
       */
      String token = headerAuth.substring(7);
      if (token != null && jwtUtil.validateToken(token)) {
        /*
         * kalau valid, kita bisa get email untuk authenticate email tsb
         */
        String email = jwtUtil.getEmailFromToken(token);
        if (email != null) {
          UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(email);
          UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,
              userDetails.getPassword(), userDetails.getAuthorities());

          authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

          SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
      }
    }

    response.setHeader("Access-Control-Allow-Origin", "*"); // set url FEnya atau clientnya, * bisa diakses semua client
    response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
    response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
    filterChain.doFilter(request, response);
  }

}
