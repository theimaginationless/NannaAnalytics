package com.theimless.nannaanalytics.admin.init;

import com.theimless.nannaanalytics.admin.property.AdminProperties;
import com.theimless.nannaanalytics.authentication.user.security.service.RegisterService;
import com.theimless.nannaanalytics.common.user.model.Role;
import com.theimless.nannaanalytics.common.user.model.RoleAuthority;
import com.theimless.nannaanalytics.common.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminInitialize implements CommandLineRunner {

    private final RegisterService registerService;
    private final AdminProperties adminProperties;

    @Override
    public void run(String... args) throws Exception {
        if (!adminProperties.isCreate()) {
            log.info("Skipping initialize with master admin...");
            return;
        }

        var masterAdmin = User.builder()
                .name(adminProperties.getName())
                .password(adminProperties.getPassword())
                .email(adminProperties.getEmail())
                .build();
        registerService.register(masterAdmin, Collections.singleton(new Role(RoleAuthority.ROLE_ADMIN)));
    }
}
