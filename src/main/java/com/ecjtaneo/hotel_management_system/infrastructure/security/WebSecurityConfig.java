package com.ecjtaneo.hotel_management_system.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        return http
                .cors(cors -> {})
                .csrf(c -> c.disable())
                .formLogin(f -> f.disable())
                .httpBasic(h -> h.disable())
                .logout(l -> l.disable())
                .authorizeHttpRequests(a -> a
                        .requestMatchers("/auth/logout").authenticated()
                        .requestMatchers("/auth/**").permitAll()

                        .requestMatchers(HttpMethod.DELETE, "/bookings/**").hasAuthority("ADMIN")
                        .requestMatchers("/bookings/confirm/**").hasAuthority("ADMIN")
                        .requestMatchers("/bookings/complete/**").hasAuthority("ADMIN")
                        .requestMatchers("/bookings/**").hasAnyAuthority("ADMIN", "GUEST")

                        .requestMatchers(HttpMethod.GET, "/rooms/**").hasAnyAuthority("ADMIN", "GUEST")
                        .requestMatchers("/rooms/**").hasAuthority("ADMIN")

                        .anyRequest().authenticated())
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration a) {
        return a.getAuthenticationManager();
    }
}
