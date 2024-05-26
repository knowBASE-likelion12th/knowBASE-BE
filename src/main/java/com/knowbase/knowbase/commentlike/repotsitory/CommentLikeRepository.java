package com.knowbase.knowbase.commentlike.repotsitory;

import com.knowbase.knowbase.domain.Comment;
import com.knowbase.knowbase.domain.CommentLike;
import com.knowbase.knowbase.domain.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Optional<CommentLike> findByUserAndComment(User user, Comment comment);

    Long countByComment (Comment comment); //댓글의 좋아요 갯수를 셈

    void deleteByComment(Comment comment);

/*    //좋아요 누르면 -> true로 바꿔주기
    @Transactional //데이터베이스에서 업데이트나 삭제 작업을 실행할 땐 트랜잭션이 필요함
    @Modifying
    @Query("update CommentLike cl set cl.isLike = true where cl.id = :commentLikeId")
    void serIsLikeTrue(@Param("commentLikeId") Long commentLikeId);

    //좋아요 한번 더 누르면 -> false로 바꿔주기 (취소)
    @Transactional
    @Modifying
    @Query("update CommentLike cl set cl.isLike = false where cl.id = :commentLikeId")
    void setIsLikeFalse(@Param("commentLikeId") Long commentLikeId);*/
}
