package com.knowbase.knowbase.roadmaps.service;

import com.knowbase.knowbase.roadmaps.dto.RoadmapCreateDto;
import com.knowbase.knowbase.roadmaps.dto.RoadmapUpdateDto;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import org.springframework.http.ResponseEntity;

public interface RoadmapService {
    ResponseEntity<CustomApiResponse<?>> createRoadmap(RoadmapCreateDto.Req roadmapCreateDto);

    ResponseEntity<CustomApiResponse<?>> updateRoadmap(Long roadmapId, RoadmapUpdateDto.Req roadmapUpdateDto);

    ResponseEntity<CustomApiResponse<?>> getRoadmap(Long userId);
}
