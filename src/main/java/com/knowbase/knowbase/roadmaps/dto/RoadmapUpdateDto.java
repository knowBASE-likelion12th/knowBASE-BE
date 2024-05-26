package com.knowbase.knowbase.roadmaps.dto;

import com.knowbase.knowbase.domain.Roadmap;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

public class RoadmapUpdateDto {
    @Getter @Setter @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Req {
        @NotNull(message = "작성자 Id는 비어있을 수 없습니다.")
        private Long userId;

        @NotNull(message = "로드맵 전 내용을 입력하세요")
        private String roadmapBefore;

        @NotNull(message = "로드맵 시작내용을 입력하세요")
        private String roadmapStart;

        @NotNull(message = "로드맵 후 내용을 입력하세요")
        private String roadmapAfter;

        public Roadmap toEntity() {
            return Roadmap.builder()
                    .roadmapBefore(roadmapBefore)
                    .roadmapStart(roadmapStart)
                    .roadmapAfter(roadmapAfter)
                    .build();
        }
    }

    @Getter @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UpdateRoadmap{
        private LocalDateTime updateAt;

        public UpdateRoadmap(LocalDateTime updateAt){
            this.updateAt = updateAt;
        }
    }
}
