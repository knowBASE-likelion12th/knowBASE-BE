package com.knowbase.knowbase.domain;

import com.knowbase.knowbase.util.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity @Getter @Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
        name = "HOME_STYLING_ID",
        sequenceName = "HOME_STYLING_ID_SEQ",
        initialValue = 1,
        allocationSize = 1
)
@Table(name = "HOME_STYLINGS")
public class HomeStyling extends BaseEntity {
    //홈 스타일링의 기본키
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HOME_STYLING_ID")
    @Column(name = "HOME_STYLING_ID")
    private Long homeStylingId;

    //한명의 유저는 여러개의 홈 스타일링 작성이 가능하다
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User userId;

    //홈 스타일링 제목
    @Column(name = "HOME_STYLING_TITLE")
    private String homeStylingTitle;

    //홈 스타일링 설명
    @Column(name = "HOME_STYLING_DESCRIPTION")
    private String homeStylingDescription;

    //홈 스타일링 사진
    @Column(name = "HOME_STYLING_IMAGE_PATH")
    private String homeStylingImagePath;

    //연관관계 편의 메소드
    //회원에 대한 연관관계 설정
    public void createHomeStyling(User userId) {
        this.userId = userId;
    }

    //홈 스타일링 제목 수정 메소드
    public void changeTitle(String homeStylingTitle) {
        this.homeStylingTitle = homeStylingTitle;
    }

    //홈 스타일링 설명 수정 메소드
    public void changeDescription(String homeStylingDescription) {
        this.homeStylingDescription = homeStylingDescription;
    }

    //홈 스타일링 사진 수정 메소드
    public void changeImagePath(String homeStylingImagePath) {
        this.homeStylingImagePath = homeStylingImagePath;
    }
}
