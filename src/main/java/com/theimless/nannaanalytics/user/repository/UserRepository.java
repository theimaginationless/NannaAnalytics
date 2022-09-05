package com.theimless.nannaanalytics.user.repository;

import com.theimless.nannaanalytics.common.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String name);
    Optional<User> findByNameOrEmail(String name, String email);
    Optional<User> findByEmail(String email);
}
