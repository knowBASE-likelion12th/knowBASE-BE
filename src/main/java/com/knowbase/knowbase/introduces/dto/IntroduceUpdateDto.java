package com.knowbase.knowbase.introduces.dto;

import com.knowbase.knowbase.domain.Introduce;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

public class IntroduceUpdateDto {
    @Getter @Setter @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Req {
        @NotNull(message = "작성자 id는 비어있을 수 없습니다.")
        private Long userId;

        @NotNull(message = "소개 문구를 입력해주세요.")
        private String introContent;

        @NotNull(message = "멘토링 가능한 시간을 입력해주세요")
        private String availableTime;

        @NotNull(message = "멘토링 강점을 입력해주세요")
        private String strength;

        public Introduce toEntity(){
            return Introduce.builder()
                    .introContent(introContent)
                    .availableTime(availableTime)
                    .strength(strength)
                    .build();
        }
    }

    @Getter @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UpdateIntroduce{
        private LocalDateTime updateAt;
        public UpdateIntroduce(LocalDateTime updateAt) {
            this.updateAt = updateAt;
        }
    }
}
