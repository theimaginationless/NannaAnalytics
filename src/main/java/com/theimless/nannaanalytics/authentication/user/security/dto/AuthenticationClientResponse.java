package com.theimless.nannaanalytics.authentication.user.security.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class AuthenticationClientResponse implements Serializable {
    private String token;
}
