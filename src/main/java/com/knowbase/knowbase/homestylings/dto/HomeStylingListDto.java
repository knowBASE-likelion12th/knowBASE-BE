package com.knowbase.knowbase.homestylings.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class HomeStylingListDto {

    @Getter @Builder
    @NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
    @AllArgsConstructor(access = lombok.AccessLevel.PROTECTED)
    public static class HomeStyling{
        private Long homestylingId;
        private String homestylingTitle;
        private String homestylingImagePath;
        private String homestylingDescription;
        private LocalDateTime updatedAt;
    }

    //홈스타일링 조회
    @Getter @Setter
    @Builder
    @NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
    public static class SearchHomestylingsRes{
        private List<HomeStyling> homestylings;

        public SearchHomestylingsRes(List<HomeStyling> homestylings){
            this.homestylings = homestylings;
        }
    }
}
