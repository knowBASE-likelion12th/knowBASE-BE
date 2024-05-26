package com.knowbase.knowbase.homestylings.dto;

import com.knowbase.knowbase.domain.HomeStyling;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

public class HomestylingCreateDto {
    @Getter @Setter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Req{

        @NotNull(message = "작성자 id는 비어있을 수 없습니다.")
        private Long userId;


        @NotNull(message="홈스타일링 제목은 비어있을 수 없습니다.")
        private String homestylingTitle;

        @NotNull(message = "홈스타일링 사진 경로는 비어있을 수 없습니다.")
        private String homestylingImagePath;

        @NotNull(message = "홈스타일링 설명은 비어있을 수 없습니다.")
        private String homestylingDescription;

        public HomeStyling toEntity(){
            return HomeStyling.builder()
                    .homeStylingTitle(homestylingTitle)
                    .homeStylingImagePath(homestylingImagePath)
                    .homeStylingDescription(homestylingDescription)
                    .build();
        }
    }

    @Getter @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CreateHomestyling{
        private Long homestylingId;
        private LocalDateTime createdAt;

        public CreateHomestyling(Long homestylingId, LocalDateTime createdAt){
            this.homestylingId = homestylingId;
            this.createdAt = createdAt;
        }
    }
}
