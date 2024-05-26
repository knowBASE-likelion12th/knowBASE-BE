package com.knowbase.knowbase.domain;

import com.knowbase.knowbase.util.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity @Getter @Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
        name = "PORTFOLIO_ID",
        sequenceName = "PORTFOLIO_ID_SEQ",
        initialValue = 1,
        allocationSize = 1
)
@Table(name = "PORTFOLIOS")
public class Portfolio extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PORTFOLIO_ID")
    @Column(name = "PORTFOLIO_ID")
    private Long portfolioId;

    //한명의 유저는 여러개의 포트폴리오 작성이 가능하다
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User userId;

    //포트폴리오 사진
    @Column(name = "PORTFOLIO_IMAGE_PATH")
    private String portfolioImagePath;

    //연관관계 편의 메소드
    //회원에 대한 연관관계 설정
    public void createPortfolio(User userId) {
        this.userId = userId;
    }

    //포트폴리오 사진 수정 메소드
    public void changeImagePath(String portfolioImagePath) {
        this.portfolioImagePath = portfolioImagePath;
    }
}
