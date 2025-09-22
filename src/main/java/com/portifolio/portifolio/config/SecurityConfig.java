package com.yourapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .csrf(csrf -> csrf.disable())       // for APIs (adjust if needed)
      .cors(Customizer.withDefaults())     // ✅ enable CORS using the bean above
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/api/**").permitAll() // adjust as needed
        .anyRequest().permitAll()
      );

    return http.build();
  }
}
