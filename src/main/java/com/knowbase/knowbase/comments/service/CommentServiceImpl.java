package com.knowbase.knowbase.comments.service;

import com.knowbase.knowbase.commentlike.repotsitory.CommentLikeRepository;
import com.knowbase.knowbase.comments.dto.CommentListDto;
import com.knowbase.knowbase.comments.dto.DeleteCommentDto;
import com.knowbase.knowbase.comments.dto.UpdateCommentdto;
import com.knowbase.knowbase.comments.dto.WriteCommentdto;
import com.knowbase.knowbase.comments.repository.CommentRepository;
import com.knowbase.knowbase.domain.Comment;
import com.knowbase.knowbase.domain.CommentLike;
import com.knowbase.knowbase.domain.Post;
import com.knowbase.knowbase.domain.User;
import com.knowbase.knowbase.posts.dto.PostListDto;
import com.knowbase.knowbase.posts.repository.PostRepository;
import com.knowbase.knowbase.users.repository.UserRepository;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.print.attribute.standard.MediaSize;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Builder
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentLikeRepository commentLikeRepository;

    //댓글 작성
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


    //댓글 수정
    @Override
    public ResponseEntity<CustomApiResponse<?>> updateComment(UpdateCommentdto.Req updateCommentDto) {
        //수정하려는 댓글이 DB에 존재하는지 확인
        Optional<Comment> findComment = commentRepository.findById(updateCommentDto.getCommentId());
        if(findComment.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(CustomApiResponse.createFailWithout(
                            HttpStatus.BAD_REQUEST.value(),
                            "수정하려는 댓글이 존재하지 않거나, 잘못된 요청입니다."));
        }

        //댓글의 작성자와 현재 로그인한 사용자가 일치하는지 확인
        Long commentUserId = findComment.get().getUser().getUserId();
        if(commentUserId != updateCommentDto.getUserId()) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(CustomApiResponse.createFailWithout(
                            HttpStatus.FORBIDDEN.value(),
                            "잘못된 요청입니다."));
        }

        //댓글 수정
        Comment comment = findComment.get();
        Comment savedComment = commentRepository.save(comment);
        //comment.changeContent(comment.getCommentContent());

        //응답 dto 생성
        UpdateCommentdto.UpdateComment responseDto = new UpdateCommentdto.UpdateComment(savedComment.getUpdateAt());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CustomApiResponse.createSuccess(
                        HttpStatus.OK.value(),
                        responseDto,
                        "댓글이 수정되었습니다."));
    }

    //댓글 삭제
    @Transactional
    @Override
    public ResponseEntity<CustomApiResponse<?>> deleteComment(Long commentId) {
        //해당 댓글이 DB에 존재하는 댓글인지
        Optional<Comment> findComment = commentRepository.findById(commentId);
        if(findComment.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(CustomApiResponse.createFailWithout(
                                HttpStatus.BAD_REQUEST.value(),
                                "수정하려는 댓글이 존재하지 않거나, 잘못된 요청입니다."));
        }
        // 댓글의 작성자와 현재 접속한 사용자가 같은지 확인
        if (findComment.get().getUser().getUserId() != commentId ) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(CustomApiResponse.createFailWithout(
                            HttpStatus.FORBIDDEN.value(),
                            "잘못된 요청입니다."));
        }

        //댓글 삭제 : comment 테이블의 키가 commentlike 테이블에서 참조되고 있기 때문에
        //commentlike 테이블에서 먼저 삭제 한 후
        commentLikeRepository.deleteByComment(findComment.get());
        //comment 테이블에서 삭제 해줘야함
        commentRepository.delete(findComment.get());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CustomApiResponse.createSuccess(
                        HttpStatus.OK.value(),
                        null,
                        "댓글이 삭제되었습니다."));
    }

    //해당 게시물의 달린 모든 댓글 조회
    @Override
    public ResponseEntity<CustomApiResponse<?>> getAllComment(Long postId, Long userId) {
        Optional<User> findUser = userRepository.findById(userId);
        if (findUser.isEmpty()) {
            CustomApiResponse<Void> res = CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "사용자를 찾을 수 없습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }

        // 해당 게시글이 DB에 존재하는지 확인
        Optional<Post> findPost = postRepository.findById(postId);
        if (findPost.isEmpty()) {
            CustomApiResponse<Void> res = CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "해당하는 게시글을 찾을 수 없습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }

        // 해당 PostId를 가진 게시물에 달린 댓글들 찾기
        List<Comment> findComments = commentRepository.findByPost(findPost.get());
        List<CommentListDto.CommentDto> commentResponse = new ArrayList<>();

        for (Comment comment : findComments) {
            Optional<CommentLike> findCommentLike = commentLikeRepository.findByUserAndComment(findUser.get(), comment);
            boolean isLiked = findCommentLike.isPresent(); //좋아요 존재 여부 가져오기
            Long likeCount = commentLikeRepository.countByComment(comment); //좋아요 갯수 가져오기

            commentResponse.add(CommentListDto.CommentDto.builder()
                    .commentId(comment.getCommentId())
                    .userId(comment.getUser().getUserId())
                    .nickname(comment.getUser().getNickname())
                    .profImgPath(comment.getUser().getProfImgPath())
                    .isMentor(comment.getUser().getIsMentor())
                    .commentContent(comment.getCommentContent())
                    .likeCount(likeCount)
                    .isLike(isLiked)
                    .build());
        }

        CommentListDto.SearchCommentRes searchCommentRes = new CommentListDto.SearchCommentRes(commentResponse);
        CustomApiResponse<CommentListDto.SearchCommentRes> res = CustomApiResponse.createSuccess(HttpStatus.OK.value(), searchCommentRes, "해당 게시물의 모든 댓글 조회 성공");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }


    //내가 쓴 댓글 조회
    @Override
    public ResponseEntity<CustomApiResponse<?>> getMyComment(Long userId) {
        //해당 유저가 DB에 존재하는 유저인지
        Optional<User> findUser = userRepository.findById(userId);
        if(findUser.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.FORBIDDEN.value(),
                            "존재하지 않는 회원입니다."));
        }

        //해당 유저가 쓴 댓글 DB에서 찾기
        List<Comment> findComment = commentRepository.findByUser(findUser.get());

        List<CommentListDto.CommentDto> commentResponse = new ArrayList<>();

        for(Comment comment : findComment){
            Optional<CommentLike> findCommentLike = commentLikeRepository.findByUserAndComment(findUser.get(), comment);
            boolean isLiked = findCommentLike.isPresent();
            Long likeCount = commentLikeRepository.countByComment(comment); //좋아요 갯수 가져오기

            commentResponse.add(CommentListDto.CommentDto.builder()
                    .commentId(comment.getCommentId())
                    .userId(comment.getUser().getUserId())
                    .nickname(comment.getUser().getNickname())
                    .profImgPath(comment.getUser().getProfImgPath())
                    .isMentor(comment.getUser().getIsMentor())
                    .commentContent(comment.getCommentContent())
                    .likeCount(likeCount)
                    .isLike(isLiked)
                    .build());
        }

        CommentListDto.SearchCommentRes searchCommentRes = new CommentListDto.SearchCommentRes(commentResponse);
        CustomApiResponse<CommentListDto.SearchCommentRes> res = CustomApiResponse.createSuccess(HttpStatus.OK.value(), searchCommentRes, "해당 유저가 쓴 댓글 조회 성공");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
