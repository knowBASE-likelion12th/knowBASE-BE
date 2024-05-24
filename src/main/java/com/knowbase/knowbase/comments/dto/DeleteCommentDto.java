package com.knowbase.knowbase.comments.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DeleteCommentDto {
    @NotNull(message = "유저의 기본키가 필요합니다.")
    private Long userId;
    @NotNull(message = "댓글의 기본키가 필요합니다.")
    private Long commentId;
}
