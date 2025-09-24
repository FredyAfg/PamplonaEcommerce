package com.codePamplonaEcomerc.pamplonaecomerc.repository;

import com.codePamplonaEcomerc.pamplonaecomerc.entity.User;
import com.codePamplonaEcomerc.pamplonaecomerc.entity.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findFirtsByEmail(String email);
    User findByRole(UserRole userRole);
}
