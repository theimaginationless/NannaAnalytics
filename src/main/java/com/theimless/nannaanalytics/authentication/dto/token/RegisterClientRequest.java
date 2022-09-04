package com.theimless.nannaanalytics.authentication.dto.token;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterClientRequest implements Serializable {
    @Size(min = 4)
    @Pattern(regexp = "^[(A-Za-z0-9_\\-)]+$")
    private String name;
    @JsonProperty(required = false)
    private String description;
    @Size(min = 5)
    @Pattern(regexp = "[^\\s]+$")
    private String password;
    @Size(min = 4)
    private String email;
    @JsonProperty(required = false)
    private String invite;
}
