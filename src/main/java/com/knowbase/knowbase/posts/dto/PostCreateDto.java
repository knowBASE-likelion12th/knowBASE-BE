package com.knowbase.knowbase.posts.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.knowbase.knowbase.domain.Post;
import com.knowbase.knowbase.domain.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;


public class PostCreateDto
{
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Req {
        @NotNull(message = "작성자 id는 비어있을 수 없습니다.")
        private Long userId;

        @NotNull(message = "게시물의 제목을 입력해주세요.")
        private String postTitle;

        @NotNull(message = "게시물의 내용을 입력해주세요.")
        private String postContent;

        public Post toEntity(String postImgPath) {
            return Post.builder()
                    .postTitle(postTitle)
                    .postContent(postContent)
                    .postImgPath(postImgPath)
                    .build();
        }
    }
}

