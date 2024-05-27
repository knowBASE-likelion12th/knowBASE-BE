package com.knowbase.knowbase.homestylings.dto;

import com.knowbase.knowbase.domain.HomeStyling;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

public class HomestylingUpdateDto {
    @Getter @Setter @Builder
    @AllArgsConstructor(access = lombok.AccessLevel.PROTECTED)
    @NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
    public static class Req{
        @NotNull(message = "작성자 id는 비어있을 수 없습니다.")
        private Long userId;

        @NotEmpty(message = "홈스타일링 제목은 비어있을 수 없습니다.")
        private String homestylingTitle;

        @NotEmpty(message = "홈스타일링 설명은 비어있을 수 없습니다.")
        private String homestylingDescription;

        @NotEmpty(message = "홈스타일링 사진 경로는 비어있을 수 없습니다.")
        private String homestylingImagePath;

        public HomeStyling toEntity(){
            return HomeStyling.builder()
                    .homeStylingTitle(homestylingTitle)
                    .homeStylingDescription(homestylingDescription)
                    .homeStylingImagePath(homestylingImagePath)
                    .build();
        }
    }
}
