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
@Table(name="POSTS")
public class Post extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name="POST_ID")
    private Long postId;

    //한명의 유저는 여러개의 게시글을 작성할 수 있다.
    //게시글이 '다'에 해당
    @ManyToOne
    @JoinColumn(name="USER_ID")
    private User userId;

    @Column(name="POST_TITLE")
    private String postTitle;

    @Column(name="POST_CONTENT")
    private String postContent;

    @Column(name="POST_IMG_PATH")
    private String postImgPath;

    // 연관관계 편의 메소드
    // 회원에 대한 연관관계 설정
    public void createPost(User userId) {
        this.userId = userId;
    }
}
