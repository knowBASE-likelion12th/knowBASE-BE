package com.knowbase.knowbase.comments.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

public class UpdateCommentdto {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Req {
        @NotNull(message = "댓글 작성자의 기본키를 작성해주세요")
        private Long userId;

        @NotNull(message = "댓글의 기본키를 작성해주세요")
        private Long commentId;

        @NotEmpty(message = "수정할 댓글 내용이 비어있습니다.")
        private String commentContent;
    }

/*    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UpdateComment {
        private LocalDateTime updateAt;

        public UpdateComment(LocalDateTime updateAt) {
            this.updateAt = updateAt;
        }
    }*/
}
