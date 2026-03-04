package com.gobusit.role.repository;

import com.gobusit.role.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, String> {
    UserRole findByUser(String userId);
    void deleteByUserId(String userId);
}
