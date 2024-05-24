package com.knowbase.knowbase.comments.service;

import com.knowbase.knowbase.comments.dto.WriteCommentdto;
import com.knowbase.knowbase.domain.Comment;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import org.springframework.http.ResponseEntity;

public interface CommentService {

    ResponseEntity<CustomApiResponse<?>> writeComment(WriteCommentdto.Req writeCommentDto);
}

