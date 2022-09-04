package com.theimless.nannaanalytics.admin.mapper;

import com.theimless.nannaanalytics.admin.dto.IssueInviteResponse;
import com.theimless.nannaanalytics.authentication.user.security.model.Invite;
import com.theimless.nannaanalytics.authentication.user.security.property.JwtProperties;
import com.theimless.nannaanalytics.common.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static com.theimless.nannaanalytics.util.ValidationUtils.checkField;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InviteMapper {

    private final JwtProperties jwtProperties;

    public IssueInviteResponse getResponse(Invite invite) {
        validateInvite(invite);
        var expiresCalendar = Calendar.getInstance();
        expiresCalendar.setTime(invite.getCreatedTime());
        expiresCalendar.add(Calendar.DAY_OF_YEAR, jwtProperties.getTokenExpirationAfterDays());
        return IssueInviteResponse.builder()
                .created(invite.getCreatedTime())
                .expires(expiresCalendar.getTime())
                .issuedBy(invite.getIssuedBy().getName())
                .inviteToken(invite.getToken())
                .build();
    }

    private boolean validateInvite(Invite invite) {
        try {
            checkField(invite, "Invite");
            checkField(invite.getToken(), "Token");
            checkField(invite.getLifeTime(), "LifeTime");
            checkField(invite.getCreatedTime(), "CreatedTime");
            validateUser(invite.getIssuedBy());
        } catch (NoSuchFieldException e) {
            log.error("Validation failed", e);
            return false;
        }

        return true;
    }

    private void validateUser(User user) throws NoSuchFieldException {
        checkField(user, "User");
        checkField(user.getName(), "Name");
        checkField(user.getEmail(), "Email");
        checkField(user.getPassword(), "Password");
    }
}
