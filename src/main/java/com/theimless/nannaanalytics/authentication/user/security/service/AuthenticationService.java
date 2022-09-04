package com.theimless.nannaanalytics.authentication.user.security.service;

import com.theimless.nannaanalytics.authentication.user.security.dto.AuthenticationClientRequest;
import com.theimless.nannaanalytics.common.exception.rest.BadRequestException;
import com.theimless.nannaanalytics.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthenticationService {

    private final UserService userService;
    private final JwtTokenService tokenService;

    public String getToken(AuthenticationClientRequest request) {
        var user = userService.getUserByName(request.getUsername());
        if (user == null) {
            throw new BadRequestException("User not found!");
        }

        var auth = new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword(),
                user.getRoles()
        );

        return tokenService.generateToken(auth);
    }
}
