package com.knowbase.knowbase.roadmaps.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class RoadmapDto {
    private String roadmapBefore;
    private String roadmapStart;
    private String roadmapAfter;
}
