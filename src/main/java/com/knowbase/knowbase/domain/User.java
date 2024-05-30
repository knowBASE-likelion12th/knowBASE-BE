package com.knowbase.knowbase.domain;

import com.knowbase.knowbase.util.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(
        name = "USER_ID", //식별자 생성기 이름
        sequenceName = "USER_ID_SEQ", //DB에 등록되어 있는 Sequence 이름
        initialValue = 1, //처음 시작 value 설정
        allocationSize = 1 //Sequence 한번 호출 시 증가하는 수
        //allocationSize가 기본값이 50이므로 1로 설정하지 않을 시, sequence 호출 시 마다 50씩 증가
)
@Table(name="USERS")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "USER_ID"
    )
    @Column(name="USER_ID")
    private Long userId;

    @Column(name="USER_NAME")
    private String userName;

    @Column(name="PASSWORD")
    private String password;

    @Column(name="NAME")
    private String name;

    @Column(name="IS_MENTOR")
    private Boolean isMentor;

    @Column(name="NICKNAME")
    private String nickname;

    @Column(name="EMPLOYMENT_PATH")
    private String employmentPath;

    @Setter
    @Column(name="PROF_IMG_PATH")
    private String profImgPath;

    @Setter
    @Column(name="MENTORING_PATH")
    private String mentoringPath;

    @Column(name="MENTOR_CONTENT")
    private String mentorContent;

    @Column(name="GENDER")
    private Boolean gender;

    @Column(name="AGE")
    private String age;

    @Column(name="INTEREST")
    private String interest;

    @Column(name="HOUSING_TYPE")
    private String housingType;

    @Column(name="SPACE_TYPE")
    private String spaceType;

    @Column(name="MENTO_STYLE")
    private String mentoStyle;


    public void changeUserName(String userName) {
        this.userName = userName;
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }


    public void changeProfImgPath(String profImgPath) {
        this.profImgPath = profImgPath;
    }

    public void changeMentoringPath(String mentoringPath) {
        this.mentoringPath = mentoringPath;
    }

    public void changeMentorContent(String mentorContent) {
        this.mentorContent = mentorContent;
    }
}
