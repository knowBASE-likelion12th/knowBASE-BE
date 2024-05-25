package com.knowbase.knowbase.domain;

import com.knowbase.knowbase.util.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity @Getter @Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
        name = "INTRO_ID",
        sequenceName = "INTRODUCE_ID_SEQ",
        initialValue = 1,
        allocationSize = 1
)
@Table(name = "INTRODUCES")
public class Introduce extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INTRO_ID")
    @Column(name = "INTRO_ID")
    private Long introId;

    // 한명의 유저는 한개의 자기소개를 작성할 수 있다.
    @OneToOne
    @JoinColumn(name = "USER_ID")
    private User userId;

    //소개 내용
    @Column(name = "INTRO_CONTENT")
    private String introContent;

    //가능한 시간대
    @Column(name = "AVAILABLE_TIME")
    private String availableTime;

    //강점
    @Column(name = "STRENGTH")
    private String strength;

    //카카오아이디
    @Column(name = "KAKAO_ID")
    private String kakaoId;

    //연관관계 편의 메소드
    //회원에 대한 연관관계 설정
    public void createIntroduce(User userId) {
        this.userId = userId;
    }

    //소개 내용 수정 메소드
    public void changeIntroContent(String introContent) {
        this.introContent = introContent;
    }

    //가능한 시간대 수정 메소드
    public void changeAvailableTime(String availableTime) {
        this.availableTime = availableTime;
    }

    //강점 수정 메소드
    public void changeStrength(String strength) {
        this.strength = strength;
    }
}
