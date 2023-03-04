package com.theimless.nannaanalytics.authentication.user.service;

import com.theimless.nannaanalytics.authentication.user.security.model.Invite;
import com.theimless.nannaanalytics.authentication.user.security.property.InviteProperties;
import com.theimless.nannaanalytics.authentication.user.security.repository.InviteRepository;
import com.theimless.nannaanalytics.authentication.user.security.service.InviteService;
import com.theimless.nannaanalytics.common.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class InviteServiceTest {

    @InjectMocks
    private InviteService inviteService;

    @Mock
    private InviteRepository inviteRepository;

    @Mock
    private InviteProperties inviteProperties;

    private User user;

    private Invite invite;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        user = User.builder()
                .id(1L)
                .build();
        invite = Invite.builder()
                .id(1L)
                .token("invite-token")
                .issuedBy(user)
                .createdTime(GregorianCalendar.getInstance().getTime())
                .isActive(true)
                .lifeTime(30)
                .build();
    }

    @Test
    public void testCreateToken() {
        User user = new User();
        String expectedToken = "sampleToken";
        when(inviteProperties.getLifeTime()).thenReturn(60);
        when(inviteRepository.save(any(Invite.class))).thenReturn(Invite.builder()
                .token(expectedToken)
                .issuedBy(user)
                .createdTime(new Date())
                .isActive(true)
                .lifeTime(60)
                .build());

        String token = inviteService.createToken(user);

        assertEquals(expectedToken, token);
    }

    @Test
    public void testCreateInvite() {
        User user = new User();
        String expectedToken = "sampleToken";
        when(inviteProperties.getLifeTime()).thenReturn(60);
        when(inviteRepository.save(any(Invite.class))).thenReturn(Invite.builder()
                .token(expectedToken)
                .issuedBy(user)
                .createdTime(new Date())
                .isActive(true)
                .lifeTime(60)
                .build());

        Invite invite = inviteService.createInvite(user);

        assertEquals(expectedToken, invite.getToken());
        assertTrue(invite.isActive());
    }

    @Test
    public void testApplyInviteReturnsFalseWhenInviteNotFound() {
        when(inviteRepository.findByToken(anyString())).thenReturn(null);

        assertFalse(inviteService.applyInvite("non-existing-token"));
    }

    @Test
    public void testApplyInviteReturnsFalseWhenInviteIsExpired() {
        invite.setCreatedTime(getPastDate(2));
        invite.setLifeTime(2);
        when(inviteRepository.findByToken(anyString())).thenReturn(invite);

        assertFalse(inviteService.applyInvite(invite.getToken()));
    }

    @Test
    public void testApplyInviteReturnsFalseWhenInviteIsNotActive() {
        invite.isActive(false);
        when(inviteRepository.findByToken(anyString())).thenReturn(invite);

        assertFalse(inviteService.applyInvite(invite.getToken()));
    }

    @Test
    public void testUseInviteReturnsFalseWhenInviteIsNull() {
        assertFalse(inviteService.applyInvite(null));
    }

    @Test
    public void testCheckInviteReturnsFalseWhenInviteIsNull() {
        assertFalse(inviteService.isActiveInvite(null));
    }

    @Test
    public void testCheckInviteReturnsFalseWhenInviteIsExpired() {
        invite.setCreatedTime(getPastDate(2));
        assertFalse(inviteService.isActiveInvite(invite.getToken()));
    }

    @Test
    public void testCheckInviteReturnsFalseWhenInviteIsNotActive() {
        invite.isActive(false);
        assertFalse(inviteService.isActiveInvite(invite.getToken()));
    }

    private Date getPastDate(int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -amount);
        return calendar.getTime();
    }
}
