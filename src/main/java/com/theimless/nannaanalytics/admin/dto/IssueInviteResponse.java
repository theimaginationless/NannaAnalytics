package com.theimless.nannaanalytics.admin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
public class IssueInviteResponse implements Serializable {
    private String issuedBy;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date created;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date expires;
    private String inviteToken;
}
