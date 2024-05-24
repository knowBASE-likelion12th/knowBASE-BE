package com.knowbase.knowbase.comments.controller;


import com.knowbase.knowbase.comments.dto.WriteCommentdto;
import com.knowbase.knowbase.comments.service.CommentService;
import com.knowbase.knowbase.domain.Comment;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CustomApiResponse<?>> writeComment(@Valid @RequestBody WriteCommentdto.Req writeCommentDto) {
        return commentService.writeComment(writeCommentDto);
    }




}
