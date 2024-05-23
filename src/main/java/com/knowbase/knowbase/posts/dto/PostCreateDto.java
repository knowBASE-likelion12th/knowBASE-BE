package com.knowbase.knowbase.posts.dto;

import com.knowbase.knowbase.domain.Post;
import com.knowbase.knowbase.domain.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;


public class PostCreateDto
{
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Req {
        @NotNull(message = "작성자 id는 비어있을 수 없습니다.")
        private Long userId;

        @NotNull(message = "게시물의 제목을 입력해주세요.")
        private String postTitle;

        @NotNull(message = "게시물의 내용을 입력해주세요.")
        private String postContent;

        private String postImgPath ;

        public Post toEntity(){
            return Post.builder()
                    .postTitle(postTitle)
                    .postContent(postContent)
                    .postImgPath(postImgPath)
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CreatPost {
        private Long postId;
        private LocalDateTime createdAt;

        public CreatPost(Long postId, LocalDateTime createdAt) {
            this.postId = postId;
            this.createdAt = createdAt;
        }
    }

}

