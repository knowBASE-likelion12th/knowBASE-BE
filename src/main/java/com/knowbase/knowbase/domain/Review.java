package com.knowbase.knowbase.domain;

import com.knowbase.knowbase.util.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SequenceGenerator(
        name = "REVIEW_ID", //식별자 생성기 이름
        sequenceName = "REVIEW_ID_SEQ", //DB에 등록되어 있는 Sequence 이름
        initialValue = 1, //처음 시작 value 설정
        allocationSize = 1 //Sequence 한번 호출 시 증가하는 수
        //allocationSize가 기본값이 50이므로 1로 설정하지 않을 시, sequence 호출 시 마다 50씩 증가
)
@Table(name="REVIEW")
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "REVIEW_ID"
    )

    @Column(name="REVIEW_ID")
    private Long reviewId;

    @Column(name="REVIEW_TITLE")
    private String reviewTitle;

    @Column(name="NICKNAME")
    private String nickname;

    @Column(name="date")
    private String date;

    @Column(name="BEFORE_RE_IMG_PATH")
    private String beforeReImgPath;

    @Column(name="AFTER_RE_IMG_PATH")
    private String afterReImgPath;

    @Column(name="REVIEW_CONTENT")
    private String reviewContent;

    @Column(name="SATISFACTION")
    private Long satisfaction;

    @Column(name="PERIOD")
    private String period;

    @Column(name="BUDGET")
    private String budget;

    @ManyToOne //후기가 '다'에 해당
    @JoinColumn(name="MENTEE_ID")
    private User menteeId;

    @ManyToOne
    @JoinColumn(name="MENTOR_ID")
    private User mentorId;

    //연관관계 설정
    public void createReview(User mentorId, User menteeId) {
        this.mentorId = mentorId;
        this.menteeId = menteeId;
    }
}
