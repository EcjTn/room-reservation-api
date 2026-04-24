package com.ecjtaneo.room_reservation_api.infrastructure.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        return http
                .cors(Customizer.withDefaults())
                .csrf(c -> c
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .ignoringRequestMatchers("/auth/register"))

                .formLogin(f -> f
                        .loginProcessingUrl("/auth/login")
                        .successHandler((req, res, auth) -> {res.setStatus(HttpServletResponse.SC_OK);})
                        .failureHandler((req, res, auth) -> {res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);})
                )

                .httpBasic(AbstractHttpConfigurer::disable)

                .logout(l -> l
                        .logoutUrl("/auth/logout")
                        .logoutSuccessHandler((req, res, auth) -> {res.setStatus(HttpServletResponse.SC_OK);}))

                .authorizeHttpRequests(a -> a
                        .requestMatchers("/auth/logout").authenticated()
                        .requestMatchers("/auth/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/bookings").hasAnyAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/bookings/**").hasAuthority("ADMIN")
                        .requestMatchers("/bookings/confirm/**").hasAuthority("ADMIN")
                        .requestMatchers("/bookings/complete/**").hasAuthority("ADMIN")
                        .requestMatchers("/bookings/**").hasAnyAuthority("ADMIN", "GUEST")

                        .requestMatchers(HttpMethod.GET, "/rooms/**").hasAnyAuthority("ADMIN", "GUEST")
                        .requestMatchers("/rooms/**").hasAuthority("ADMIN")

                        .requestMatchers("/reports/**").hasAuthority("ADMIN")

                        .anyRequest().authenticated())
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:5173"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
