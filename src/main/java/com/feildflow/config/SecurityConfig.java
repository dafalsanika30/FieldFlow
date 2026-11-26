
package com.feildflow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import jakarta.servlet.DispatcherType;
import com.feildflow.security.JwtAuthenticationFilter;
@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                // Public authentication
                .requestMatchers("/api/auth/**").permitAll()

                // Service requests
                .requestMatchers(HttpMethod.POST, "/api/requests")
                    .hasAnyRole("REQUESTER", "ADMIN")

                .requestMatchers(HttpMethod.PATCH,
                        "/api/requests/*/approve",
                        "/api/requests/*/reject")
                    .hasAnyRole("PLANNER", "ADMIN")

                .requestMatchers(HttpMethod.GET, "/api/requests/**")
                    .hasAnyRole("REQUESTER", "PLANNER", "SUPERVISOR",
                                "TECHNICIAN", "ADMIN")

                // Work orders
                .requestMatchers(HttpMethod.POST, "/api/work-orders")
                    .hasAnyRole("PLANNER", "ADMIN")

                .requestMatchers(HttpMethod.PATCH,
                        "/api/work-orders/*/verify",
                        "/api/work-orders/*/close")
                    .hasAnyRole("SUPERVISOR", "ADMIN")

                // Assignments
                .requestMatchers(HttpMethod.POST, "/api/assignments")
                    .hasAnyRole("PLANNER", "ADMIN")

                .requestMatchers(HttpMethod.PATCH, "/api/assignments/**")
                    .hasAnyRole("TECHNICIAN", "ADMIN")

                // All other endpoints require authentication
                .anyRequest().authenticated()
            )

            .exceptionHandling(exception -> exception
                .authenticationEntryPoint((request, response, authException) -> {
                    
                    response.sendError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized");
                })
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    
                    response.sendError(HttpStatus.FORBIDDEN.value(), "Forbidden");
                })
            )
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable())
            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
