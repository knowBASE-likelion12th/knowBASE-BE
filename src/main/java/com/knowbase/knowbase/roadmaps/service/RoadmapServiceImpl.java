package com.knowbase.knowbase.roadmaps.service;

import com.knowbase.knowbase.domain.Introduce;
import com.knowbase.knowbase.domain.Roadmap;
import com.knowbase.knowbase.domain.User;
import com.knowbase.knowbase.introduces.dto.IntroduceListDto;
import com.knowbase.knowbase.roadmaps.dto.RoadmapCreateDto;
import com.knowbase.knowbase.roadmaps.dto.RoadmapListDto;
import com.knowbase.knowbase.roadmaps.dto.RoadmapUpdateDto;
import com.knowbase.knowbase.roadmaps.repository.RoadmapRepository;
import com.knowbase.knowbase.users.repository.UserRepository;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Builder
public class RoadmapServiceImpl implements RoadmapService {
    private final RoadmapRepository roadmapRepository;
    private final UserRepository userRepository;

    //로드맵 작성
    @Override
    public ResponseEntity<CustomApiResponse<?>> createRoadmap(RoadmapCreateDto.Req roadmapCreateDto) {
        //게시글 작성자가 DB에 존재하는지 확인
        Optional<User> findUser = userRepository.findById(roadmapCreateDto.getUserId());
        if(findUser.isEmpty()){
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.FORBIDDEN.value(),
                            "해당 유저는 존재하지 않습니다."));
        }
        Roadmap newRoadmap = roadmapCreateDto.toEntity();
        newRoadmap.createRoadmap(findUser.get()); //연관관계 설정
        Roadmap savedRoadmap = roadmapRepository.save(newRoadmap);

        //응답 dto
        RoadmapCreateDto.CreateRoadmap createdRoadmapResponse = new RoadmapCreateDto.CreateRoadmap(savedRoadmap.getRoadmapId(),savedRoadmap.getCreateAt());
        //응답
        CustomApiResponse<RoadmapCreateDto.CreateRoadmap> res = CustomApiResponse.createSuccess(HttpStatus.OK.value(), createdRoadmapResponse,"로드맵이 작성되었습니다.");
        return ResponseEntity.ok(res);
    }

    //로드맵 수정
    @Override
    public ResponseEntity<CustomApiResponse<?>> updateRoadmap(Long roadmapId, RoadmapUpdateDto.Req roadmapCreateDto) {
        //1.수정하려는 로드맵이 DB에 존재하는지 확인
        Optional<Roadmap> findRoadmap = roadmapRepository.findById(roadmapId);
        if (findRoadmap.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(CustomApiResponse.createFailWithout(
                            HttpStatus.BAD_REQUEST.value(),
                            "해당 로드맵은 존재하지 않습니다."));
        }

        //게시물의 작성자와 수정하려는 유저가 일치하는지 확인
        if (!findRoadmap.get().getUserId().getUserId().equals(roadmapCreateDto.getUserId())) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(CustomApiResponse.createFailWithout(
                            HttpStatus.FORBIDDEN.value(),
                            "해당 로드맵을 수정할 권한이 없습니다."));
        }

        //로드맵 수정
        Roadmap roadmap = findRoadmap.get();
        roadmap.changeBefore(roadmapCreateDto.getRoadmapBefore());
        roadmap.changeStart(roadmapCreateDto.getRoadmapStart());
        roadmap.changeAfter(roadmapCreateDto.getRoadmapAfter());

        //응답 dto 생성
        RoadmapUpdateDto.UpdateRoadmap updatedRoadmapResponse = RoadmapUpdateDto.UpdateRoadmap.builder()
                .updateAt(roadmap.getUpdateAt())
                .build();

        //응답
        CustomApiResponse<RoadmapUpdateDto.UpdateRoadmap> res = CustomApiResponse.createSuccess(HttpStatus.OK.value(), updatedRoadmapResponse, "로드맵이 수정되었습니다.");
        return ResponseEntity.ok(res);
    }

    //특정 유저의 로드맵 조회
    @Override
    public ResponseEntity<CustomApiResponse<?>> getRoadmap(Long userId) {

        Optional<User> findUser = userRepository.findById(userId);

        //해당 userId를 가진 유저가 쓴 게시물 찾기
        List<Roadmap> findRoadmap = roadmapRepository.findByUserId(findUser.get());

        //응답 dto 생성
        List<RoadmapListDto.RoadmapDto> roadmapResponse = new ArrayList<>();
        for(Roadmap roadmap : findRoadmap){
            roadmapResponse.add(RoadmapListDto.RoadmapDto.builder()
                    .roadmapId(roadmap.getRoadmapId())
                    .roadmapBefore(roadmap.getRoadmapBefore())
                    .roadmapStart(roadmap.getRoadmapStart())
                    .roadmapAfter(roadmap.getRoadmapAfter())
                    .updatedAt(roadmap.getUpdateAt())
                    .build());
        }

        //응답
        RoadmapListDto.SearchRoadmapsRes searchRoadmapsRes = new RoadmapListDto.SearchRoadmapsRes(roadmapResponse);
        CustomApiResponse<List<RoadmapListDto.SearchRoadmapsRes>> res = CustomApiResponse.createSuccess(HttpStatus.OK.value(), Collections.singletonList(searchRoadmapsRes), "로드맵이 조회되었습니다.");
        return ResponseEntity.ok(res);
    }

}