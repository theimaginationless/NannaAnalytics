package com.theimless.nannaanalytics.admin.property;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties("nannaanalytics.admin.master")
public class AdminProperties {
    private boolean create;
    private String name;
    private String password;
    private String email;
}
