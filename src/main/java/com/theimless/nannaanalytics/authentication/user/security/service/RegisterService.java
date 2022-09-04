package com.theimless.nannaanalytics.authentication.user.security.service;

import com.theimless.nannaanalytics.common.user.model.Role;
import com.theimless.nannaanalytics.common.user.model.User;
import com.theimless.nannaanalytics.common.exception.rest.BadRequestException;
import com.theimless.nannaanalytics.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
public class RegisterService {
    private InviteService inviteService;
    private UserService userService;
    private JwtTokenService tokenService;

    @Autowired
    public RegisterService(InviteService inviteService,
                           UserService userService,
                           JwtTokenService tokenService) {
        this.inviteService = inviteService;
        this.userService = userService;
        this.tokenService = tokenService;
    }

    public String registerByInvite(User user, Set<Role> roles) {
        if (!inviteService.isActiveInvite(user.getInvitedByToken())) {
            log.error("Token '{}' is not active", user.getInvitedByToken());
            throw new BadRequestException("Invalid invite token");
        }
        var existUser = userService.getUserByNameOrEmail(user.getName(), user.getEmail());
        if (existUser != null) {
            log.debug("User '{}' already registered", user.getName());
            throw new BadRequestException(String.format("User '%s' with email '%s' already registered",
                    user.getName(),
                    user.getEmail())
            );
        }

        if (!inviteService.applyInvite(user.getInvitedByToken())) {
            log.error("Applying invite to user '{}' was failed with token '{}'",
                    user.getName(),
                    user.getInvitedByToken()
            );
            throw new BadRequestException("Invalid invite token");
        }

        user.setRoles(roles);
        var newUser = userService.saveUser(user);
        log.info("User '{}' with invite token '{}' created successfully",
                newUser.getName(),
                newUser.getInvitedByToken()
        );

        var auth = new UsernamePasswordAuthenticationToken(
                user.getName(),
                null,
                user.getRoles()
        );
        return tokenService.generateToken(auth);
    }

    // TODO убрать дубликаты
    public String register(User user, Set<Role> roles) {
        var existUser = userService.getUserByNameOrEmail(user.getName(), user.getEmail());
        if (existUser != null) {
            log.debug("User '{}' already registered", user.getName());
            throw new BadRequestException(String.format("User '%s' with email '%s' already registered",
                    user.getName(),
                    user.getEmail())
            );
        }

        user.setRoles(roles);
        var newUser = userService.saveUser(user);
        log.info("User '{}' with invite token '{}' created successfully",
                newUser.getName(),
                newUser.getInvitedByToken()
        );

        var auth = new UsernamePasswordAuthenticationToken(
                user.getName(),
                null,
                user.getRoles()
        );
        return tokenService.generateToken(auth);
    }
}
