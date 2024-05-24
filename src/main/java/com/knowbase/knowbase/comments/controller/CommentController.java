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

    @PostMapping
    public ResponseEntity<CustomApiResponse<?>> writeComment(@Valid @RequestBody WriteCommentdto.Req writeCommentDto) {
        return commentService.writeComment(writeCommentDto);
    }

    @PutMapping
    public ResponseEntity<CustomApiResponse<?>> updateComment(@Valid @RequestBody UpdateCommentdto.Req updateCommentDto) {
        return commentService.updateComment(updateCommentDto);
    }

    @DeleteMapping
    public ResponseEntity<CustomApiResponse<?>> deleteComment(@RequestBody @Valid DeleteCommentDto deleteCommentDto){
        return commentService.deleteComment(deleteCommentDto);
    }
}
