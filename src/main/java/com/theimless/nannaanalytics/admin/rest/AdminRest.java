package com.theimless.nannaanalytics.admin.rest;

import com.theimless.nannaanalytics.admin.AdminService;
import com.theimless.nannaanalytics.admin.dto.IssueInviteResponse;
import com.theimless.nannaanalytics.admin.dto.UpdateUserRolesRequest;
import com.theimless.nannaanalytics.admin.dto.UpdateUserRolesResponse;
import com.theimless.nannaanalytics.admin.mapper.InviteMapper;
import com.theimless.nannaanalytics.authentication.user.security.dto.AuthenticationClientResponse;
import com.theimless.nannaanalytics.authentication.user.security.service.RegisterService;
import com.theimless.nannaanalytics.common.dto.BaseResponse;
import com.theimless.nannaanalytics.common.user.model.Role;
import com.theimless.nannaanalytics.common.user.model.RoleAuthority;
import com.theimless.nannaanalytics.authentication.dto.token.RegisterClientRequest;
import com.theimless.nannaanalytics.authentication.dto.token.mapper.RegisterClientRequestMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@Slf4j
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@PreAuthorize("hasAnyAuthority(T(com.theimless.nannaanalytics.common.user.model.RoleAuthority).ROLE_ADMIN)")
public class AdminRest {

    private final AdminService adminService;
    private final InviteMapper inviteMapper;
    private final RegisterClientRequestMapper requestMapper;
    private final RegisterService registerService;

    @PostMapping("/issueInvite")
    public ResponseEntity<IssueInviteResponse> issueInvite() {
        var invite = adminService.issueInvite();
        var response = inviteMapper.getResponse(invite);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/registerAdmin")
    public ResponseEntity<AuthenticationClientResponse> registerAdmin(
            @RequestBody @Validated RegisterClientRequest request
    ) {
        var user = requestMapper.getUser(request);
        if (user == null) {
            log.error("Mapping failed!");
            return ResponseEntity.badRequest().build();
        }

        var token = registerService.register(
                user,
                Collections.singleton(new Role(RoleAuthority.ROLE_ADMIN))
        );
        var rs = AuthenticationClientResponse.builder()
                .token(token)
                .build();
        return ResponseEntity.ok(rs);
    }

    @PostMapping("/updateRoles")
    public ResponseEntity<BaseResponse> updateRoles(@RequestBody UpdateUserRolesRequest request) {
        var result = adminService.updateRoles(
                request.getUsername(),
                request.getAddRoles(),
                request.getRemoveRoles()
        );

        if (result == null) {
            log.error("Update roles unsuccessful!");
            return ResponseEntity.ok(BaseResponse.builder().StatusCode(1).build());
        }

        var response = UpdateUserRolesResponse.builder()
                .username(result.getName())
                .roles(result.getRoles())
                .StatusCode(0)
                .build();

        return ResponseEntity.ok(response);
    }
}
