package com.knowbase.knowbase.roadmaps.controller;

import com.knowbase.knowbase.roadmaps.dto.RoadmapCreateDto;
import com.knowbase.knowbase.roadmaps.dto.RoadmapUpdateDto;
import com.knowbase.knowbase.roadmaps.service.RoadmapService;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/roadmap")
public class RoadmapController {
    private final RoadmapService roadmapService;

    // 로드맵 작성
    @PostMapping
    public ResponseEntity<CustomApiResponse<?>> createRoadmap(
            @Valid @RequestBody RoadmapCreateDto.Req roadmapCreateDto){
        ResponseEntity<CustomApiResponse<?>> roadmap = roadmapService.createRoadmap(roadmapCreateDto);
        return roadmap;
    }

    // 로드맵 수정
    @PatchMapping("/{roadmapId}")
    public ResponseEntity<CustomApiResponse<?>> updateRoadmap(
            @PathVariable("roadmapId") Long roadmapId,
            @RequestBody RoadmapUpdateDto.Req roadmapUpdateDto) {
        ResponseEntity<CustomApiResponse<?>> roadmap = roadmapService.updateRoadmap(roadmapId, roadmapUpdateDto);
        return roadmap;
    }

    // 로드맵 조회
    @GetMapping
    public ResponseEntity<CustomApiResponse<?>> getRoadmapDetail(
            @RequestParam("userId") Long userId) {
        ResponseEntity<CustomApiResponse<?>> roadmap = roadmapService.getRoadmap(userId);
        return roadmap;
    }
}
