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
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@SequenceGenerator(
        name = "CATEGORY_SEQ_GENERATOR",
        sequenceName = "CATEGORY_SEQ",
        initialValue = 1, allocationSize = 1
)
@Table(name = "CATEGORIES")
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CATEGORY_SEQ_GENERATOR")
    @Column(name = "CATEGORY_ID")
    private Long categoryId;

    //하나의 멘토는 하나의 카테고리를 가질 수 있다
    @OneToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(name="INTEREST")
    private String interest;

    @Column(name="HOUSING_TYPE")
    private String housingType;

    @Column(name="SPACE_TYPE")
    private String spaceType;

    @Column(name="STYLE")
    private String style;

    //연관관계 편의 메소드
    //멘토에 대한 연관관계 설정
    public void createCategory(User user) {
        this.user = user;
    }
}
