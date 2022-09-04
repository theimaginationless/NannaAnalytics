package com.theimless.nannaanalytics.admin.dto;

import com.theimless.nannaanalytics.common.user.model.Role;
import lombok.Data;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

@Data
public class UpdateUserRolesRequest implements Serializable {
    @Size(min = 4)
    private String username;
    private Set<Role> addRoles;
    private Set<Role> removeRoles;
}
