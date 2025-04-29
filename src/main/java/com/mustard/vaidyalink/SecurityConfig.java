package com.mustard.vaidyalink;

import com.mustard.vaidyalink.filters.JwtRequestFilter;
import com.mustard.vaidyalink.services.MyUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;
    private final MyUserDetailsService myUserDetailsService;

    public SecurityConfig(JwtRequestFilter jwtRequestFilter, MyUserDetailsService myUserDetailsService) {
        this.jwtRequestFilter = jwtRequestFilter;
        this.myUserDetailsService = myUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/users/login",
                                "/api/institutions/login",
                                "/api/users/register",
                                "/api/institutions/register",
                                "/api/status",
                                "/api/users/invite",
                                "/api/users/validate-token",
                                "/api/institutions/validate-token",
                                "/api/institutions/logout",
                                "/api/users/logout",
                                "/api/access-requests/request",
                                "/api/access-requests/approve-access",
                                "/api/users/access-requests",
                                "/api/users/delete-account",
                                "/api/users/revoke-access")
                )
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration corsConfiguration = new CorsConfiguration();
                    corsConfiguration.setAllowedOrigins(List.of("http://localhost:3000"));
                    corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    corsConfiguration.setAllowedHeaders(List.of("*"));
                    corsConfiguration.setAllowCredentials(true);
                    return corsConfiguration;
                }))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/static/**", "/favicon.ico", "/api/users/login", "/api/institutions/login",
                                "/api/institutions/register", "/api/status", "/api/access-requests/approve-access",
                                "/api/users/records", "/api/users/access-requests", "/api/users/delete-account",
                                "/api/users/revoke-access").permitAll()
                        .requestMatchers("/api/users/invite").authenticated() // Ensure invite endpoint requires authentication
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(myUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
