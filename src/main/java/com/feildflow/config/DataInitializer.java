
package com.feildflow.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.feildflow.entity.Role;
import com.feildflow.entity.User;
import com.feildflow.repository.UserRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner createDemoUsers(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {
            createUser(userRepository, passwordEncoder,
                    "Demo Requester", "requester@test.com",
                    Role.REQUESTER);

            createUser(userRepository, passwordEncoder,
                    "Demo Planner", "planner@test.com",
                    Role.PLANNER);

            createUser(userRepository, passwordEncoder,
                    "Demo Technician", "technician@test.com",
                    Role.TECHNICIAN);

            createUser(userRepository, passwordEncoder,
                    "Demo Supervisor", "supervisor@test.com",
                    Role.SUPERVISOR);
        };
    }

    private void createUser(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            String fullName,
            String email,
            Role role) {

        if (!userRepository.existsByEmail(email)) {
            User user = new User();
            user.setFullName(fullName);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode("test123"));
            user.setRole(role);
            user.setActive(true);

            userRepository.save(user);
        }
    }
}
