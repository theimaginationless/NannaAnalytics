package com.theimless.nannaanalytics.authentication.user.security.dto;

import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
public class AuthenticationClientRequest implements Serializable {
    @Size(min = 4)
    @Pattern(regexp = "^[(A-Za-z0-9_\\-)]+$")
    private String username;
    private String password;
}
