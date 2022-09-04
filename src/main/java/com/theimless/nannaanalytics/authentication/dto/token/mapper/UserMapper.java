package com.theimless.nannaanalytics.authentication.dto.token.mapper;

import com.theimless.nannaanalytics.common.user.model.User;
import com.theimless.nannaanalytics.authentication.dto.token.RegisterClientResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.theimless.nannaanalytics.util.ValidationUtils.checkField;

@Slf4j
@Component
public class UserMapper {
    public RegisterClientResponse getResponse(User user) {
        validateUser(user);

        return RegisterClientResponse.builder()
                .build();
    }

    private boolean validateUser(User user) {
        try {
            checkField(user, "User");
            checkField(user.getName(), "Name");
            checkField(user.getDescription(), "Description");
            checkField(user.getEmail(), "Email");
            checkField(user.getPassword(), "Password");
        } catch (NoSuchFieldException e) {
            log.error("Validation failed", e);
            return false;
        }

        return true;
    }
}
