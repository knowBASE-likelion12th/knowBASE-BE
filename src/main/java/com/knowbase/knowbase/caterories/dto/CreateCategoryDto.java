package com.knowbase.knowbase.caterories.dto;

import com.knowbase.knowbase.domain.Category;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

public class CreateCategoryDto {

    @Getter
    @Setter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Req {
        @NotNull(message = "작성자 id는 비어있을 수 없습니다.")
        private Long userId;

        @NotNull(message = "관심사는 비어있을 수 없습니다.")
        private String interest;

        @NotNull(message = "주거형태는 비어있을 수 없습니다")
        private String housingType;

        @NotNull(message = "공간 유형은 비어있을 수 없습니다.")
        private String spaceType;

        @NotNull(message = "스타일은 비어있을 수 없습니다.")
        private String style;

        public Category toEntity() {
            return Category.builder()
                    .housingType(housingType)
                    .spaceType(spaceType)
                    .style(style)
                    .interest(interest)
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CreateCategory {
        private Long categoryId;
        private LocalDateTime createdAt;

        public CreateCategory(Long categoryId, LocalDateTime createdAt) {
            this.categoryId = categoryId;
            this.createdAt = createdAt;
        }
    }
}
