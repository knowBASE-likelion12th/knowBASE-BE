package com.knowbase.knowbase.roadmaps.service;

import com.knowbase.knowbase.roadmaps.dto.RoadmapDto;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import org.springframework.http.ResponseEntity;

public interface RoadmapService {
    ResponseEntity<CustomApiResponse<?>> createRoadmap(Long userId, RoadmapDto roadmapDto);
    ResponseEntity<CustomApiResponse<?>> updateRoadmap(Long roadmapId, RoadmapDto roadmapDto);
    ResponseEntity<CustomApiResponse<?>> getRoadmapDetail(Long roadmapId);
}
