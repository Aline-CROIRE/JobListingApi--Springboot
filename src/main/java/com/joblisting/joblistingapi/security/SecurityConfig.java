package com.joblisting.joblistingapi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity(debug = true) // The debug flag is useful for diagnosing issues
public class SecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    // ==============================================================================================
    // FILTER CHAIN #1: For Public Authentication Endpoints (/api/auth/**)
    // This chain has the highest priority (@Order(1)) and minimal security.
    // ==============================================================================================
    @Bean
    @Order(1)
    public SecurityFilterChain authFilterChain(HttpSecurity http) throws Exception {
        http
                // This chain only applies to URLs starting with /api/auth/
                .securityMatcher(new AntPathRequestMatcher("/api/auth/**"))
                // Disable CSRF for these endpoints
                .csrf(AbstractHttpConfigurer::disable)
                // Permit all requests to these endpoints without authentication
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                // Ensure no session is created (as it's stateless)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
    // ==============================================================================================


    // ==============================================================================================
    // FILTER CHAIN #2: For All Other Protected API Endpoints
    // This chain has a lower priority (@Order(2)) and handles all JWT and role-based security.
    // ==============================================================================================
    @Bean
    @Order(2)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception { // <-- TYPO FIXED HERE
        http
                // Disable CSRF for our stateless API
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Public GET requests for jobs are allowed
                        .requestMatchers(HttpMethod.GET, "/api/jobs", "/api/jobs/**").permitAll()
                        // Role-based authorization for protected endpoints
                        .requestMatchers(HttpMethod.POST, "/api/jobs").hasRole("EMPLOYER")
                        .requestMatchers(HttpMethod.GET, "/api/jobs/*/applicants").hasRole("EMPLOYER")
                        .requestMatchers(HttpMethod.POST, "/api/jobs/*/apply").hasRole("APPLICANT")
                        // Any other request that doesn't match the above must be authenticated
                        .anyRequest().authenticated()
                )
                // We use JWTs, so session management should be stateless
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Add our custom JWT filter to the chain
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    // ==============================================================================================

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}