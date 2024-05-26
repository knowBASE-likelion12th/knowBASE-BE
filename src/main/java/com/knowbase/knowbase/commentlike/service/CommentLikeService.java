package com.knowbase.knowbase.commentlike.service;

import com.knowbase.knowbase.commentlike.dto.CommentLikeDto;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import org.springframework.http.ResponseEntity;

public interface CommentLikeService {
    ResponseEntity<CustomApiResponse<?>> isLike(CommentLikeDto.Req commentLikeDto);

    ResponseEntity<CustomApiResponse<?>> isUnlike(CommentLikeDto.Req commentLikeDto);
}
