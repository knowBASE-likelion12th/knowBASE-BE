package com.knowbase.knowbase.introduces.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class IntroduceListDto {

    @Getter @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class IntroduceDto {
        private Long introId;
        private String introContent;
        private String availableTime;
        private String strength;
        private String snsId;
    }

    //게시물 조회
    @Getter @Setter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SearchIntroducesRes {
        private List<IntroduceDto> introduces;

        public SearchIntroducesRes(List<IntroduceDto> introduces) {
            this.introduces = introduces;
        }
    }
}
