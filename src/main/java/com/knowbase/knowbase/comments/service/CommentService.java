package com.knowbase.knowbase.comments.service;

import com.knowbase.knowbase.comments.dto.DeleteCommentDto;
import com.knowbase.knowbase.comments.dto.UpdateCommentdto;
import com.knowbase.knowbase.comments.dto.WriteCommentdto;
import com.knowbase.knowbase.domain.Comment;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import org.springframework.http.ResponseEntity;

public interface CommentService {

    ResponseEntity<CustomApiResponse<?>> writeComment(WriteCommentdto.Req writeCommentDto);

    ResponseEntity<CustomApiResponse<?>> updateComment(UpdateCommentdto.Req updateCommentDto);

    ResponseEntity<CustomApiResponse<?>> deleteComment(DeleteCommentDto commentId);

    ResponseEntity<CustomApiResponse<?>> getAllComment(Long postId);

    ResponseEntity<CustomApiResponse<?>> getMyComment(Long userId);
}

