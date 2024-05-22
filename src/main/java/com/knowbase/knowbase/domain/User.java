package com.knowbase.knowbase.domain;

import com.knowbase.knowbase.util.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="USERS")
public class User extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name="USER_ID")
    private long userId;

    @Column(name="USER_NAME")
    private String userName;

    @Column(name="PASSWORD")
    private String password;

    @Column(name="NAME")
    private String name;

    @Column(name="IS_MENTOR")
    private Boolean isMentor;

    @Column(name="EMPLOYMENT_PATH")
    private String employmentPath;

    @Column(name="PROF_IMG_PATH")
    private String profImgPath;

    @Column(name="MENTORING_PATH")
    private String mentoringPath;

    @Column(name="MENTOR_CONTENT")
    private String mentorContent;
}
