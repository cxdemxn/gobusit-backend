package com.gobusit.user.dto;

import java.util.List;

public record UserResponse(
    String  id,
    String  email,
    String  firstName,
    String  lastName,
    String  phoneNumber,
    boolean active,
    List<String> roles
) {}