package com.knowbase.knowbase.comments.service;

import com.knowbase.knowbase.comments.dto.WriteCommentdto;
import com.knowbase.knowbase.comments.repository.CommentRepository;
import com.knowbase.knowbase.domain.Comment;
import com.knowbase.knowbase.domain.Post;
import com.knowbase.knowbase.domain.User;
import com.knowbase.knowbase.posts.repository.PostRepository;
import com.knowbase.knowbase.users.repository.UserRepository;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Builder
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Override
    public ResponseEntity<CustomApiResponse<?>> writeComment(WriteCommentdto.Req writeCommentDto) {
        //1. 댓글 작성자가 DB에 존재하는지
        Optional<User> findUser = userRepository.findById(writeCommentDto.getUserId());
        //존재하지 않으면
        if(findUser.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.FORBIDDEN.value(),
                            "존재하지 않는 회원입니다."));
        }
        // 1.2. 게시글(postsId)이 실제로 존재하는 게시글인지 확인
        // 삭제된 게시글에 댓글을 달려고 시도 할 수 있음
        Optional<Post> findPost = postRepository.findById(writeCommentDto.getPostId());
        if (findPost.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.FORBIDDEN.value(),
                            "삭제되었거나 존재하지 않는 게시글입니다."));
        }

        //댓글 엔티티 작성
        Comment createComment = Comment.builder()
                .commentContent(writeCommentDto.getCommentContent())
                .build();
        //연관관계 설정
        createComment.createComment(findUser.get(),findPost.get());

        //댓글 엔티티 저장
        Comment savedComment = commentRepository.save(createComment);

        //응답 dto 생성
        WriteCommentdto.WriteComment responseDto = new WriteCommentdto.WriteComment(savedComment.getCommentId(),savedComment.getCreateAt());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CustomApiResponse.createSuccess(
                        HttpStatus.OK.value(),
                        responseDto,
                        "댓글이 작성 되었습니다."));

    }
}
