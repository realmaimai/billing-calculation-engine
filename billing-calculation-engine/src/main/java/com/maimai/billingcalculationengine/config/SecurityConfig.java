package com.maimai.billingcalculationengine.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Slf4j
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("Security Filter Chain starting...");
        http
                // Keep your existing JWT authentication if needed
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll())  // Or configure more specific rules
                .csrf(csrf -> csrf.disable());  // This disables CSRF protection

        return http.build();
    }
}