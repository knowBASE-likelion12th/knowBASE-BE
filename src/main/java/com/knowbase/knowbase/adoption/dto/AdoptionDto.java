package com.knowbase.knowbase.adoption.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

public class AdoptionDto {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Req{
        @NotNull(message = "채택 할 댓글의 ID를 넣어주세요")
        private Long commentId;

        @NotNull(message = "채택 하는 유저의 ID를 넣어주세요")
        private Long userId;
    }
}
