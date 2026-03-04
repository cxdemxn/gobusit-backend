package com.gobusit.user.dto;

import jakarta.validation.constraints.NotBlank;

public record ChangeRoleRequest(
        @NotBlank String roleName
) {}