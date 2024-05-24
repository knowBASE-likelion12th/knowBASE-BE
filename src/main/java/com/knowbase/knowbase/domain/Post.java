package com.knowbase.knowbase.domain;

import com.knowbase.knowbase.util.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SequenceGenerator(
        name = "POST_ID", //식별자 생성기 이름
        sequenceName = "POST_ID_SEQ", //DB에 등록되어 있는 Sequence 이름
        initialValue = 1, //처음 시작 value 설정
        allocationSize = 1 //Sequence 한번 호출 시 증가하는 수
        //allocationSize가 기본값이 50이므로 1로 설정하지 않을 시, sequence 호출 시 마다 50씩 증가
)
@Table(name="POSTS")
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "POST_ID"
    )
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

    //연관관계에 있는 모든 댓글 얻어오기
    //하나의 게시글 - 여러개의 댓글 => 일 대 다
    @OneToMany(mappedBy = "post")
    private List<Comment> commentList = new ArrayList<>();

    // 연관관계 편의 메소드
    // 회원에 대한 연관관계 설정
    public void createPost(User userId) {
        this.userId = userId;
    }

    // 게시글 제목 수정 메소드
    public void changePostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    // 게시글 내용 수정 메소드
    public void changePostContent(String postContent) {
        this.postContent = postContent;
    }

    // 게시글 작성자 수정 메소드
    public void changePostImgPath(String postImgPath) {
        this.postImgPath = postImgPath;
    }

}
