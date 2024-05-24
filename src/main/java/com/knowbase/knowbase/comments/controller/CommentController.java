package com.knowbase.knowbase.comments.controller;


import com.knowbase.knowbase.comments.dto.DeleteCommentDto;
import com.knowbase.knowbase.comments.dto.UpdateCommentdto;
import com.knowbase.knowbase.comments.dto.WriteCommentdto;
import com.knowbase.knowbase.comments.service.CommentService;
import com.knowbase.knowbase.domain.Comment;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    //댓글 작성
    @PostMapping
    public ResponseEntity<CustomApiResponse<?>> writeComment(@Valid @RequestBody WriteCommentdto.Req writeCommentDto) {
        return commentService.writeComment(writeCommentDto);
    }

    //댓글 수정
    @PutMapping
    public ResponseEntity<CustomApiResponse<?>> updateComment(@Valid @RequestBody UpdateCommentdto.Req updateCommentDto) {
        return commentService.updateComment(updateCommentDto);
    }

    //댓글 삭제
    @DeleteMapping
    public ResponseEntity<CustomApiResponse<?>> deleteComment(@RequestBody @Valid DeleteCommentDto deleteCommentDto){
        return commentService.deleteComment(deleteCommentDto);
    }

    //댓글 조회
    @GetMapping
    public ResponseEntity<CustomApiResponse<?>> getComment(@RequestParam("postId") Long postId){
        return commentService.getComment(postId);
    }

    //내가 쓴 댓글 조회
    @GetMapping
    public ResponseEntity<CustomApiResponse<?>> getMyComment(@RequestParam("userId") Long userId){
        return commentService.getMyComment(userId);
    }
}
