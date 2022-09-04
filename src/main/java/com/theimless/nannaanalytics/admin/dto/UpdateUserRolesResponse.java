package com.theimless.nannaanalytics.admin.dto;

import com.theimless.nannaanalytics.common.dto.BaseResponse;
import com.theimless.nannaanalytics.common.user.model.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class UpdateUserRolesResponse extends BaseResponse implements Serializable {
    private String username;
    private Set<Role> roles;
}
