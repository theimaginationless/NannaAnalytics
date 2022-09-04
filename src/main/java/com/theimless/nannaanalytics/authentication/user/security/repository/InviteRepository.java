package com.theimless.nannaanalytics.authentication.user.security.repository;

import com.theimless.nannaanalytics.authentication.user.security.model.Invite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InviteRepository extends JpaRepository<Invite, Long> {
    Invite findByToken(String token);
}
