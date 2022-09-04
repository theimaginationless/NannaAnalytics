package com.theimless.nannaanalytics.authentication.dto.token;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class RegisterClientResponse implements Serializable {
    private String token;
}
