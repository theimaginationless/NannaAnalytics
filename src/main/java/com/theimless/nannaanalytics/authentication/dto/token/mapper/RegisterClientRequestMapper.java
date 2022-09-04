package com.theimless.nannaanalytics.authentication.dto.token.mapper;

import com.theimless.nannaanalytics.common.user.model.User;
import com.theimless.nannaanalytics.authentication.dto.token.RegisterClientRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.theimless.nannaanalytics.util.ValidationUtils.checkField;

@Slf4j
@Component
public class RegisterClientRequestMapper {
    public User getUser(RegisterClientRequest request) {
        if (!validateRequest(request)) {
            return null;
        }

        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .invitedByToken(request.getInvite())
                .build();
    }

    private boolean validateRequest(RegisterClientRequest request) {
        try {
            checkField(request, "Request");
            checkField(request.getName(), "Name");
            checkField(request.getEmail(), "Email");
            checkField(request.getPassword(), "Password");
        } catch (NoSuchFieldException e) {
            log.error("Validation failed", e);
            return false;
        }

        return true;
    }
}
