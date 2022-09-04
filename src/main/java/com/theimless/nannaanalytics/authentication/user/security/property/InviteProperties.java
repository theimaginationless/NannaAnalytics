package com.theimless.nannaanalytics.authentication.user.security.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties("nannaanalytics.authentication.invite")
public class InviteProperties {
    // Minute
    private Integer lifeTime;
}
