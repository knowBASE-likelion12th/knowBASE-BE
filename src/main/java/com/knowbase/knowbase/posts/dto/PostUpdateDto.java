package com.knowbase.knowbase.posts.dto;

import com.knowbase.knowbase.domain.Post;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

public class PostUpdateDto {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Req{
            @NotNull(message = "작성자 id는 비어있을 수 없습니다.")
            private Long userId;

            @NotNull(message = "수정할 제목을 입력해주세요.")
            private String postTitle;

            @NotNull(message = "수정할 내용을 입력해주세요.")
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
    public static class UpdatePost {
        private LocalDateTime updateAt;
        public UpdatePost(LocalDateTime updateAt) {
            this.updateAt = updateAt;
        }
    }

}
