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
        name = "COMMENTLIKE_ID", //식별자 생성기 이름
        sequenceName = "COMMENTLIKE_ID_SEQ", //DB에 등록되어 있는 Sequence 이름
        initialValue = 1, //처음 시작 value 설정
        allocationSize = 1 //Sequence 한번 호출 시 증가하는 수
        //allocationSize가 기본값이 50이므로 1로 설정하지 않을 시, sequence 호출 시 마다 50씩 증가
)
@Table(name="COMMENTLIKE")
public class CommentLike extends BaseEntity{
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "COMMENTLIKE_ID"
    )
    @Column(name = "COMMENTLIKE_ID")
    private Long id;

    @Column(name = "IS_LIKE")
    private Boolean isLike = false; //디폴트를 false로 (좋아요x)

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "COMMENT_ID")
    private Comment comment;

    //유저와 댓글에 대한 연관관계 설정
    public void createCommentLike(User user, Comment comment) {
        this.user = user;
        this.comment = comment;
    }
}
