package com.knowbase.knowbase.comments.dto;

import com.knowbase.knowbase.posts.dto.PostListDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


public class CommentListDto {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static  class CommentDto{
        private Long commentId; // 댓글 기본키
        private Long userId; // 댓글 작성자 기본키(수정,삭제에 사용)
        private String userName; // 댓글 작성자 아이디
        private String commentContent; // 댓글 내용
        private LocalDateTime updatedAt; // 최종 수정 날짜
    }
    @Getter
    @Setter
    @NoArgsConstructor
    public static class SearchCommentRes{
        private List<CommentDto> comments;

        public SearchCommentRes(List<CommentDto> comments) {
            this.comments = comments;
        }
    }
}
