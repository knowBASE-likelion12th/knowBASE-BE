package com.knowbase.knowbase.posts.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class PostListDto {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class PostDto {
        private Long postId;
        private String postTitle;
        private String postContent;
        private String postImgPath;
        private String nickname;
        private Long userId;
        private String postAuthorProfImg;
        private LocalDateTime updatedAt;
    }

    // 게시글 조회 : List<Post> posts
    @Getter @Setter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SearchPostsRes {
        private List<PostDto> posts;

        public SearchPostsRes(List<PostDto> posts) {
            this.posts = posts;
        }
    }

}
