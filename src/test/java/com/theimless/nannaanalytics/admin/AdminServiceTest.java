package com.theimless.nannaanalytics.admin;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.Set;

import com.theimless.nannaanalytics.authentication.user.security.model.Invite;
import com.theimless.nannaanalytics.authentication.user.security.service.InviteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.theimless.nannaanalytics.common.user.model.Role;
import com.theimless.nannaanalytics.common.user.model.User;
import com.theimless.nannaanalytics.user.service.UserService;

class AdminServiceTest {

    @Mock
    UserService userService;

    @Mock
    InviteService inviteService;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    Authentication authentication;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getName()).thenReturn("user");
    }

    @Test
    void testUpdateRoles() {
        String username = "testuser";
        Set<Role> addRoles = new HashSet<>();
        addRoles.add(new Role("ADMIN"));
        Set<Role> removeRoles = new HashSet<>();
        removeRoles.add(new Role("USER"));

        User user = new User();
        user.setName(username);
        when(userService.isUserExist(username)).thenReturn(true);
        when(userService.updateRoles(username, addRoles, removeRoles)).thenReturn(user);

        AdminService adminService = new AdminService(userService, inviteService, authenticationManager);
        User result = adminService.updateRoles(username, addRoles, removeRoles);

        verify(userService).isUserExist(username);
        verify(userService).updateRoles(username, addRoles, removeRoles);
        assertEquals(user, result);
    }

    @Test
    void testIssueInvite() {
        User user = new User();
        user.setName("user");
        when(userService.getUserByName("user")).thenReturn(user);

        Invite invite = new Invite();
        when(inviteService.createInvite(user)).thenReturn(invite);

        AdminService adminService = new AdminService(userService, inviteService, authenticationManager);
        Invite result = adminService.issueInvite();

        verify(userService).getUserByName("user");
        verify(inviteService).createInvite(user);
        assertEquals(invite, result);
    }

}
