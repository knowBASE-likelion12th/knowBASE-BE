package com.knowbase.knowbase.roadmaps.controller;

import com.knowbase.knowbase.roadmaps.dto.RoadmapDto;
import com.knowbase.knowbase.roadmaps.service.RoadmapService;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/roadmap")
public class RoadmapController {
    private final RoadmapService roadmapService;

    // 로드맵 작성
    @PostMapping("/create")
    public ResponseEntity<CustomApiResponse<?>> createRoadmap(
            @RequestParam("userId") Long userId,
            @RequestBody RoadmapDto roadmapDto) {
        return roadmapService.createRoadmap(userId, roadmapDto);
    }

    // 로드맵 수정
    @PutMapping("/update/{roadmapId}")
    public ResponseEntity<CustomApiResponse<?>> updateRoadmap(
            @PathVariable("roadmapId") Long roadmapId,
            @RequestBody RoadmapDto roadmapDto) {
        return roadmapService.updateRoadmap(roadmapId, roadmapDto);
    }

    // 로드맵 조회
    @GetMapping("/{roadmapId}")
    public ResponseEntity<CustomApiResponse<?>> getRoadmapDetail(
            @PathVariable("roadmapId") Long roadmapId) {
        return roadmapService.getRoadmapDetail(roadmapId);
    }
}
