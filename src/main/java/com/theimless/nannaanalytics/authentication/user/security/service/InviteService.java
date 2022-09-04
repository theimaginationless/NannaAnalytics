package com.theimless.nannaanalytics.authentication.user.security.service;

import com.theimless.nannaanalytics.authentication.user.security.model.Invite;
import com.theimless.nannaanalytics.common.user.model.User;
import com.theimless.nannaanalytics.authentication.user.security.property.InviteProperties;
import com.theimless.nannaanalytics.authentication.user.security.repository.InviteRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

@Slf4j
@Service
public class InviteService {
    private InviteRepository inviteRepository;
    private InviteProperties inviteProperties;

    @Autowired
    public InviteService(InviteRepository inviteRepository,
                         InviteProperties inviteProperties) {
        this.inviteRepository = inviteRepository;
        this.inviteProperties = inviteProperties;
    }

    public boolean applyInvite(String token) {
        var invite = inviteRepository.findByToken(token);
        if (invite == null) {
            log.info("Can't find invite by token '{}'", token);
            return false;
        }

        if (!checkInvite(invite)) {
            return false;
        }

        return useInvite(invite);
    }

    public boolean isActiveInvite(String token) {
        var invite = inviteRepository.findByToken(token);
        return checkInvite(invite);
    }

    public String createToken(User user) {
        return createInvite(user).getToken();
    }

    public Invite createInvite(User user) {
        var invite = Invite.builder()
                .token(UUID.randomUUID().toString().replace("-", ""))
                .issuedBy(user)
                .createdTime(GregorianCalendar.getInstance().getTime())
                .isActive(true)
                .lifeTime(inviteProperties.getLifeTime())
                .build();
        return inviteRepository.save(invite);
    }

    private boolean useInvite(Invite invite) {
        if (invite == null) {
            log.error("Invite is null");
            return false;
        }

        invite.isActive(false);
        inviteRepository.save(invite);
        return true;
    }

    private boolean checkInvite(Invite invite) {
        if (invite == null) {
            log.error("Invite is null");
            return false;
        }

        if (!invite.isActive()) {
            log.debug("Invite is not active. Token={}", invite.getToken());
            return false;
        }

        long endTokenTime = DateUtils.addMinutes(invite.getCreatedTime(), invite.getLifeTime()).getTime();
        long diffTime = endTokenTime - new Date().getTime();
        if (diffTime <= 0) {
            log.debug("Invite was expired. Token={}", invite.getToken());
            return false;
        }

        return true;
    }
}
