package com.theimless.nannaanalytics.user.repository;

import com.theimless.nannaanalytics.common.user.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
