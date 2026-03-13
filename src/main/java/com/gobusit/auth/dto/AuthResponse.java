package com.gobusit.auth.dto;

import com.gobusit.user.entity.User;

import java.util.List;

public record AuthResponse(
        String token,
        User user,
        List<String> roles
) {}
