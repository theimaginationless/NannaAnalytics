package com.theimless.nannaanalytics.authentication.rest;

import com.theimless.nannaanalytics.authentication.user.security.dto.AuthenticationClientRequest;
import com.theimless.nannaanalytics.authentication.user.security.dto.AuthenticationClientResponse;
import com.theimless.nannaanalytics.authentication.user.security.service.AuthenticationService;
import com.theimless.nannaanalytics.authentication.user.security.service.RegisterService;
import com.theimless.nannaanalytics.common.user.model.Role;
import com.theimless.nannaanalytics.common.user.model.RoleAuthority;
import com.theimless.nannaanalytics.authentication.dto.token.RegisterClientRequest;
import com.theimless.nannaanalytics.authentication.dto.token.mapper.RegisterClientRequestMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthenticationRest {
    private final RegisterService registerService;
    private final RegisterClientRequestMapper requestMapper;
    private final AuthenticationService authenticationService;

    @PostMapping("getToken")
    public ResponseEntity<AuthenticationClientResponse> getToken(@RequestBody AuthenticationClientRequest request) {
        var token = authenticationService.getToken(request);

        return ResponseEntity.ok(AuthenticationClientResponse.builder()
                .token(token)
                .build());
    }

    @PostMapping("/registerClient")
    public ResponseEntity<AuthenticationClientResponse> registerClient(@RequestBody RegisterClientRequest request) {
        var user = requestMapper.getUser(request);
        if (user == null) {
            log.error("Mapping failed!");
            return ResponseEntity.badRequest().build();
        }

        var token = registerService.registerByInvite(
                user,
                Collections.singleton(new Role(RoleAuthority.ROLE_PRODUCER))
        );
        var rs = AuthenticationClientResponse.builder()
                .token(token)
                .build();
        return ResponseEntity.ok(rs);
    }
}
