package com.knowbase.knowbase.commentlike.controller;

import com.knowbase.knowbase.commentlike.dto.CommentLikeDto;
import com.knowbase.knowbase.commentlike.service.CommentLikeService;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/commentlike")
@RequiredArgsConstructor
public class CommentLikeController {
    private final CommentLikeService commentLikeService;

    @PostMapping("/like")
    public ResponseEntity<CustomApiResponse<?>> isLike(@Valid @RequestBody CommentLikeDto.Req commentLikeDto) {
        return commentLikeService.isLike(commentLikeDto);
    }

    @PostMapping("/unlike")
    public ResponseEntity<CustomApiResponse<?>> isUnlike(@Valid @RequestBody CommentLikeDto.Req commentLikeDto) {
        return commentLikeService.isUnlike(commentLikeDto);
    }
}
