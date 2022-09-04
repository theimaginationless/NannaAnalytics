package com.theimless.nannaanalytics.authentication.user.security.model;

import com.theimless.nannaanalytics.common.user.model.User;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Invite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User issuedBy;
    private String description;
    private String token;
    private Date createdTime;
    private Integer lifeTime;
    @Getter
    @Setter
    @Accessors(fluent = true)
    private Boolean isActive;
}
