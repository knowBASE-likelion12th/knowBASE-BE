package com.knowbase.knowbase.roadmaps.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class RoadmapListDto {

    @Getter @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class RoadmapDto {
        private Long roadmapId;
        private String roadmapBefore;
        private String roadmapStart;
        private String roadmapAfter;
        private LocalDateTime updatedAt;
    }

    //로드맵 조회
    @Getter @Setter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SearchRoadmapsRes {
        private List<RoadmapDto> roadmap;

        public SearchRoadmapsRes(List<RoadmapDto> roadmap) {
            this.roadmap = roadmap;
        }
    }
}
