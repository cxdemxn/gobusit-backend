package com.gobusit.user.service;

import com.gobusit.role.entity.Role;
import com.gobusit.role.entity.UserRole;
import com.gobusit.role.repository.RoleRepository;
import com.gobusit.role.repository.UserRoleRepository;
import com.gobusit.user.dto.ChangeRoleRequest;
import com.gobusit.user.dto.UserResponse;
import com.gobusit.user.entity.User;
import com.gobusit.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    public List<UserResponse> findAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public UserResponse findUserById(String id) {
        return toResponse(getUser(id));
    }

    @Transactional
    public UserResponse changeRole(String userId, ChangeRoleRequest req) {
        User user = getUser(userId);

        Role newRole = roleRepository.findByName(req.roleName())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Role not found: " + req.roleName()));

        // Clear existing roles and assign the new one
        userRoleRepository.deleteByUserId(userId);

        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(newRole);
        userRoleRepository.save(userRole);

        // Refresh user to reflect new roles
        return toResponse(userRepository.findById(userId).orElseThrow());
    }

    public UserResponse disableUser(String id) {
        User user = getUser(id);
        if (!user.isActive()) {
            throw new IllegalStateException("User is already disabled");
        }
        user.setActive(false);
        return toResponse(userRepository.save(user));
    }

    public UserResponse enableUser(String id) {
        User user = getUser(id);
        if (user.isActive()) {
            throw new IllegalStateException("User is already active");
        }
        user.setActive(true);
        return toResponse(userRepository.save(user));
    }


    private User getUser(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + id));
    }

    private UserResponse toResponse(User user) {
        List<String> roles = user.getUserRoles()
                .stream()
                .map(ur -> ur.getRole().getName())
                .toList();
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.isActive(),
                roles
        );
    }
}
