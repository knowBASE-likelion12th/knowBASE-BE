package com.knowbase.knowbase.commentlike.service;

import com.knowbase.knowbase.commentlike.dto.CommentLikeDto;
import com.knowbase.knowbase.commentlike.repotsitory.CommentLikeRepository;
import com.knowbase.knowbase.comments.repository.CommentRepository;
import com.knowbase.knowbase.domain.Comment;
import com.knowbase.knowbase.domain.CommentLike;
import com.knowbase.knowbase.domain.User;
import com.knowbase.knowbase.users.repository.UserRepository;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentLikeServiceImpl implements CommentLikeService {
    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;


    //댓글 좋아요
    @Transactional
    @Override
    public ResponseEntity<CustomApiResponse<?>> isLike(CommentLikeDto.Req commentLikeDto) {
        Optional<User> findUser = userRepository.findById(commentLikeDto.getUserId());

        //해당 댓글이 DB에 존재하는지
        Optional<Comment> findComment = commentRepository.findById(commentLikeDto.getCommentId());
        if (findComment.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body(CustomApiResponse.createFailWithout(HttpStatus.FORBIDDEN.value(),
                                "삭제되었거나 존재하지 않는 댓글입니다."));
        }

        //댓글 좋아요 누르면 commentLike 테이블에 생겨야함
        Optional<CommentLike> findCommentLike = commentLikeRepository.findByUserAndComment(findUser.get(),findComment.get());

        //현재 로그인한 유저가 해당 댓글에 좋아요를 달지 않은 상태라면
        if (findCommentLike.isEmpty()) {
            CommentLike commentLike = CommentLike.builder()
                    .comment(findComment.get())
                    .user(findUser.get())
                    .build();

            commentLikeRepository.save(commentLike);
        }else {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.BAD_REQUEST.value(),
                            "이미 좋아요한 상태입니다."));
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CustomApiResponse.createSuccess(
                        HttpStatus.OK.value(),
                        null,
                        "좋아요"));

    }

    //댓글 좋아요 취소
    @Transactional
    @Override
    public ResponseEntity<CustomApiResponse<?>> isUnlike(CommentLikeDto.Req commentLikeDto) {
        Optional<User> findUser = userRepository.findById(commentLikeDto.getUserId());

        //해당 댓글이 DB에 존재하는지
        Optional<Comment> findComment = commentRepository.findById(commentLikeDto.getCommentId());
        if (findComment.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.FORBIDDEN.value(),
                            "삭제되었거나 존재하지 않는 댓글입니다."));
        }

        Optional<CommentLike> commentLike  = commentLikeRepository.findByUserAndComment(findUser.get(), findComment.get());

        if(commentLike.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.BAD_REQUEST.value(),
                            "이미 좋아요가 취소된 상태입니다."));
        }
        else {
            commentLikeRepository.delete(commentLike.get());
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CustomApiResponse.createSuccess(
                        HttpStatus.OK.value(),
                        null,
                        "좋아요 취소"));

    }
}
