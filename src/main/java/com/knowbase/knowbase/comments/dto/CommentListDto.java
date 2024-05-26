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
        private String nickname;
        private String profImgPath;
        private Boolean isMentor;
        private String commentContent;
        private Long likeCount;
        private Boolean isLike;
        private Long commentCount;
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
