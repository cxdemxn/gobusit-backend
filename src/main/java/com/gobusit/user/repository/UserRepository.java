package com.gobusit.user.repository;

import com.gobusit.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByPhoneNumber(String phoneNumber);
    boolean existsByPhoneNumber(String phoneNumber);
    @Query("""
    SELECT u FROM User u
    LEFT JOIN FETCH u.userRoles ur
    LEFT JOIN FETCH ur.role
    WHERE u.phoneNumber = :phoneNumber
""")
    Optional<User> findByPhoneNumberWithRoles(String phoneNumber);
    List<User> findByActiveTrue();
}
