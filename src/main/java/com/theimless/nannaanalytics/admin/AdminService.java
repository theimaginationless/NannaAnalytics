package com.theimless.nannaanalytics.admin;

import com.theimless.nannaanalytics.authentication.user.security.model.Invite;
import com.theimless.nannaanalytics.authentication.user.security.service.InviteService;
import com.theimless.nannaanalytics.common.user.model.Role;
import com.theimless.nannaanalytics.common.user.model.User;
import com.theimless.nannaanalytics.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminService {

    private final UserService userService;
    private final InviteService inviteService;
    private final AuthenticationManager authenticationManager;

    public User updateRoles(String username, Set<Role> add, Set<Role> remove) {
        if (!userService.isUserExist(username)) {
            log.warn("User '{}' not found", username);
            return null;
        }

        return userService.updateRoles(username, add, remove);
    }

    public Invite issueInvite() {
        var currentAuth = SecurityContextHolder.getContext().getAuthentication();
        var user = userService.getUserByName(currentAuth.getName());
        return inviteService.createInvite(user);
    }
}
