package com.gobusit.user.controller;

import com.gobusit.user.dto.ChangeRoleRequest;
import com.gobusit.user.dto.UserResponse;
import com.gobusit.user.service.AdminUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return adminUserService.findAllUsers();
    }

    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable String id) {
        return adminUserService.findUserById(id);
    }

    @PatchMapping("/{id}/roles")
    public UserResponse changeRole(
            @PathVariable String id,
            @RequestBody @Valid ChangeRoleRequest req) {
        return adminUserService.changeRole(id, req);
    }

    @PatchMapping("/{id}/disable")
    public UserResponse disableUser(@PathVariable String id) {
        return adminUserService.disableUser(id);
    }

    @PatchMapping("/{id}/enable")
    public UserResponse enableUser(@PathVariable String id) {
        return adminUserService.enableUser(id);
    }
}
