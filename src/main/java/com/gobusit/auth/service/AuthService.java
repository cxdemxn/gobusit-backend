package com.gobusit.auth.service;

import com.gobusit.auth.dto.AuthResponse;
import com.gobusit.auth.dto.LoginRequest;
import com.gobusit.auth.dto.RegisterRequest;
import com.gobusit.role.entity.Role;
import com.gobusit.role.entity.UserRole;
import com.gobusit.role.repository.RoleRepository;
import com.gobusit.role.repository.UserRoleRepository;
import com.gobusit.security.JwtService;
import com.gobusit.user.entity.User;
import com.gobusit.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;
    private final UserRoleRepository userRoleRepository;

    public AuthResponse register(RegisterRequest register) {
        if (userRepository.existsByPhoneNumber(register.phoneNumber())) {
            throw new IllegalStateException("An account with this phone number already exists");
        }

        User user = new User();
        user.setPhoneNumber(register.phoneNumber());
        user.setPassword(passwordEncoder.encode(register.password()));
        user.setEmail(register.email());
        user.setFirstName(register.firstName());
        user.setLastName(register.lastName());

        Role role = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("USER role not found - run seeds"));

        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(role);
        user.setUserRoles(List.of(userRole));

        userRepository.save(user);
        userRoleRepository.save(userRole);


        return buildTokenResponse(user);
    }

    public AuthResponse login(LoginRequest login) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        login.phoneNumber(),
                        login.password()
                )
        );

        User user = userRepository.findByPhoneNumber(login.phoneNumber()).orElseThrow();
        return buildTokenResponse(user);
    }

    private AuthResponse buildTokenResponse(User user) {
        List<String> roles = user.getUserRoles()
                .stream()
                .map(userRole -> "ROLE_" + userRole)
                .toList();

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getPhoneNumber(),
                user.getPassword(),
                List.of()
        );

        return new AuthResponse(jwtService.generateToken(userDetails, roles));
    }
}
