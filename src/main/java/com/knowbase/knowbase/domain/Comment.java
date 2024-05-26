package com.knowbase.knowbase.domain;

import com.knowbase.knowbase.util.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.lang.reflect.Member;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SequenceGenerator(
        name = "COMMENT_ID", //식별자 생성기 이름
        sequenceName = "COMMENT_ID_SEQ", //DB에 등록되어 있는 Sequence 이름
        initialValue = 1, //처음 시작 value 설정
        allocationSize = 1 //Sequence 한번 호출 시 증가하는 수
        //allocationSize가 기본값이 50이므로 1로 설정하지 않을 시, sequence 호출 시 마다 50씩 증가
)
@Table(name="COMMENTS")
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "COMMENT_ID"
    )
    @Column(name="COMMENT_ID")
    private Long commentId;

    @Column(name="COMMENT_CONTENT")
    private String commentContent;

    @Column(name="IS_LIKE")
    private Boolean isLike;

    @Column(name="LIKE_COUNT")
    private Long likeCount;

    @Column(name="IS_ADOPT")
    private Boolean adoption;

    @ManyToOne //댓글이 '다'에 해당
    @JoinColumn(name = "USER_ID")
    private User user; //외래키

    @ManyToOne //댓글이 '다'에 해당
    @JoinColumn(name = "POST_ID")
    private Post post; //외래키

    //회원과 게시글에 대한 연관관계 설정
    public void createComment(User user, Post post) {
        this.user = user;
        this.post = post;
    }
    // 댓글 수정 함수
    public void changeContent(String commentContent) {
        this.commentContent = commentContent;
    }
}
