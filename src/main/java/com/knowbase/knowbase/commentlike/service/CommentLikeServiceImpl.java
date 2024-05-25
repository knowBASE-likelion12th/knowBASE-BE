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


        CommentLike temp = commentLikeRepository.findByUserAndComment(findUser.get(), findComment.get());

        //좋아요 로그가 없을 시 -> isLike true로 변경
        if(temp == null) {
            CommentLike commentLike = CommentLike.builder()
                    .comment(findComment.get())
                    .user(findUser.get())
                    .build();
            commentLikeRepository.save(commentLike);
            commentLikeRepository.serIsLikeTrue(commentLike);
        } else if (temp.getIsLike() == null || temp.getIsLike() == false) {
            commentLikeRepository.serIsLikeFalse(temp);
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CustomApiResponse.createSuccess(
                        HttpStatus.OK.value(),
                        null,
                        "좋아요"));

    }
}
