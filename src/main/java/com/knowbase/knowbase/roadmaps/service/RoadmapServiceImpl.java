package com.knowbase.knowbase.roadmaps.service;

import com.knowbase.knowbase.domain.Roadmap;
import com.knowbase.knowbase.domain.User;
import com.knowbase.knowbase.roadmaps.dto.RoadmapDto;
import com.knowbase.knowbase.roadmaps.service.RoadmapService;
import com.knowbase.knowbase.users.repository.UserRepository;
import com.knowbase.knowbase.roadmaps.repository.RoadmapRepository;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoadmapServiceImpl implements RoadmapService {
    private final UserRepository userRepository;
    private final RoadmapRepository roadmapRepository;

    @Override
    public ResponseEntity<CustomApiResponse<?>> createRoadmap(Long userId, RoadmapDto roadmapDto) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "유저를 찾을 수 없습니다."));
        }

        User user = userOptional.get();
        Roadmap roadmap = Roadmap.builder()
                .userId(user)
                .roadmapBefore(roadmapDto.getRoadmapBefore())
                .roadmapStart(roadmapDto.getRoadmapStart())
                .roadmapAfter(roadmapDto.getRoadmapAfter())
                .build();

        roadmapRepository.save(roadmap);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CustomApiResponse.createSuccess(HttpStatus.OK.value(), null, "로드맵 작성에 성공하였습니다."));
    }

    @Override
    public ResponseEntity<CustomApiResponse<?>> updateRoadmap(Long roadmapId, RoadmapDto roadmapDto) {
        Optional<Roadmap> roadmapOptional = roadmapRepository.findById(roadmapId);
        if (roadmapOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "로드맵을 찾을 수 없습니다."));
        }

        Roadmap roadmap = roadmapOptional.get();
        roadmap.changeBefore(roadmapDto.getRoadmapBefore());
        roadmap.changeStart(roadmapDto.getRoadmapStart());
        roadmap.changeAfter(roadmapDto.getRoadmapAfter());

        roadmapRepository.save(roadmap);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CustomApiResponse.createSuccess(HttpStatus.OK.value(), null, "로드맵 수정에 성공하였습니다."));
    }

    @Override
    public ResponseEntity<CustomApiResponse<?>> getRoadmapDetail(Long roadmapId) {
        Optional<Roadmap> roadmapOptional = roadmapRepository.findById(roadmapId);
        if (roadmapOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "로드맵을 찾을 수 없습니다."));
        }

        Roadmap roadmap = roadmapOptional.get();
        RoadmapDto roadmapDto = RoadmapDto.builder()
                .roadmapBefore(roadmap.getRoadmapBefore())
                .roadmapStart(roadmap.getRoadmapStart())
                .roadmapAfter(roadmap.getRoadmapAfter())
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .body(CustomApiResponse.createSuccess(HttpStatus.OK.value(), roadmapDto, "로드맵 조회에 성공하였습니다."));
    }
}
