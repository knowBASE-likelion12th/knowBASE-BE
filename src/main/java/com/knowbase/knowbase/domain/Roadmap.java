package com.knowbase.knowbase.domain;

import com.knowbase.knowbase.util.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity @Getter @Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
        name = "ROADMAP_ID",
        sequenceName = "ROADMAP_ID_SEQ",
        initialValue = 1,
        allocationSize = 1
)
@Table(name = "ROADMAPS")
public class Roadmap extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROADMAP_ID")
    @Column(name = "ROADMAP_ID")
    private Long roadmapId;

    // 한명의 유저는 한개의 로드맵만 작성가능하다
    @OneToOne
    @JoinColumn(name = "USER_ID")
    private User userId;

    // 로드맵 전 글
    @Column(name = "ROADMAP_BEFORE")
    private String roadmapBefore;

    //로드맵 시작 글
    @Column(name = "ROADMAP_START")
    private String roadmapStart;

    // 로드맵 후 글
    @Column(name = "ROADMAP_AFTER")
    private String roadmapAfter;

    //연관관계 편의 메소드
    //회원에 대한 연관관계 설정
    public void createRoadmap(User userId) {
        this.userId = userId;
    }

    //로드맵 전 글 수정 메소드
    public void changeBefore(String roadmapBefore) {
        this.roadmapBefore = roadmapBefore;
    }

    //로드맵 시작 글 수정 메소드
    public void changeStart(String roadmapStart) {
        this.roadmapStart = roadmapStart;
    }

    //로드맵 후 글 수정 메소드
    public void changeAfter(String roadmapAfter) {
        this.roadmapAfter = roadmapAfter;
    }
}
