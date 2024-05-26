package com.knowbase.knowbase.commentlike.dto;


import jakarta.validation.constraints.NotNull;
import lombok.*;

public class CommentLikeDto {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Req{
        @NotNull(message = "좋아요 할 댓글의 ID를 넣어주세요")
        private Long commentId;

        @NotNull(message = "좋아요를 하는 유저의 ID를 넣어주세요")
        private Long userId;
    }
}
