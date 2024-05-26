package com.knowbase.knowbase.introduces.dto;

import com.knowbase.knowbase.domain.Introduce;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

public class IntroduceCreateDto {
    @Getter @Setter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Req {
        @NotNull(message = "작성자 id는 비어있을 수 없습니다.")
        private Long userId;

        @NotNull(message = "소개 문구를 입력해주세요.")
        private String introContent;

        @NotNull(message = "멘토링 가능한 시간을 입력해주세요")
        private String availableTime;

        @NotNull(message = "연락가능한 카카오톡 아이디를 적어주세요")
        private String kakaoId;

        @NotNull(message = "멘토링 강점을 입력해주세요")
        private String strength;


        public Introduce toEntity(){
            return Introduce.builder()
                    .introContent(introContent)
                    .availableTime(availableTime)
                    .kakaoId(kakaoId)
                    .strength(strength)
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CreateIntroduce {
        private Long introId;
        private LocalDateTime createdAt;

        public CreateIntroduce(Long introId, LocalDateTime createdAt) {
            this.introId = introId;
            this.createdAt = createdAt;
        }
    }
}
