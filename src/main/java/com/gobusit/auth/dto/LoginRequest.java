package com.gobusit.auth.dto;

public record LoginRequest(
        String phoneNumber,
        String password
) {}
