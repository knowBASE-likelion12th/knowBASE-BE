package com.knowbase.knowbase.comments.dto;

import com.knowbase.knowbase.posts.dto.PostListDto;
import lombok.*;

import java.util.List;

public class CommentListDto {
    @Getter @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentDto{
        private Long commentId;
        private Long userId;
        private String userName;
        private String profImgPath;
        private Boolean isMentor;
        private String commentContent;
    }
    // 댓글 조회
    @Getter @Setter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SearchCommentRes {
        private List<CommentListDto.CommentDto> comments;

        public SearchCommentRes(List<CommentListDto.CommentDto> comments) {
            this.comments = comments;
        }
    }

}
