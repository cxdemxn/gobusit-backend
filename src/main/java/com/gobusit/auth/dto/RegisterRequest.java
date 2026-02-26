package com.gobusit.auth.dto;

public record RegisterRequest(
        String phoneNumber,
        String password,
        String email,
        String firstName,
        String lastName
) {}