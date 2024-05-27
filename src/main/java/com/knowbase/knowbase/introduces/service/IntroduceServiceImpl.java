package com.knowbase.knowbase.introduces.service;

import com.knowbase.knowbase.domain.Introduce;
import com.knowbase.knowbase.domain.User;
import com.knowbase.knowbase.introduces.dto.IntroduceCreateDto;
import com.knowbase.knowbase.introduces.dto.IntroduceListDto;
import com.knowbase.knowbase.introduces.dto.IntroduceUpdateDto;
import com.knowbase.knowbase.introduces.repository.IntroduceRepository;
import com.knowbase.knowbase.users.repository.UserRepository;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Builder
public class IntroduceServiceImpl implements IntroduceService {
    private final IntroduceRepository introduceRepository;
    private final UserRepository userRepository;

    //소개글 작성
    @Override
    public ResponseEntity<CustomApiResponse<?>> createIntroduce(IntroduceCreateDto.Req introduceCreateDto) {
        try {
            // 소개글 작성자가 DB에 존재하는지 확인
            Optional<User> findUser = userRepository.findById(introduceCreateDto.getUserId());
            if (findUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "존재하지 않는 유저입니다."));
            }

            Introduce newIntroduce = introduceCreateDto.toEntity();
            newIntroduce.createIntroduce(findUser.get()); // 연관관계 설정
            Introduce savedIntroduce = introduceRepository.save(newIntroduce);

            // 응답
            CustomApiResponse<IntroduceCreateDto> res = CustomApiResponse.createSuccess(HttpStatus.OK.value(), null, "소개글이 작성되었습니다.");
            return ResponseEntity.ok(res);
        } catch (DataAccessException dae) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "데이터베이스 오류가 발생했습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 오류가 발생했습니다."));
        }
    }


    @Override
    public ResponseEntity<CustomApiResponse<?>> updateIntroduce(Long introId, IntroduceUpdateDto.Req introduceUpdateDto) {
        try {
            // 1. 수정하려는 소개글이 DB에 존재하는지 확인
            Optional<Introduce> findIntroduce = introduceRepository.findById(introId);
            if (findIntroduce.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "해당 소개글이 존재하지 않습니다."));
            }

            // 2. 게시물의 작성자와 수정하려는 유저가 일치하는지 확인
            if (!findIntroduce.get().getUserId().getUserId().equals(introduceUpdateDto.getUserId())) {
                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body(CustomApiResponse.createFailWithout(HttpStatus.FORBIDDEN.value(), "해당 소개글을 수정할 권한이 없습니다."));
            }

            // 3. 게시물 수정
            Introduce introduce = findIntroduce.get();
            introduce.changeIntroContent(introduceUpdateDto.getIntroContent());
            introduce.changeAvailableTime(introduceUpdateDto.getAvailableTime());
            introduce.changeStrength(introduceUpdateDto.getStrength());

            // 4. 응답
            CustomApiResponse<IntroduceUpdateDto> res = CustomApiResponse.createSuccess(HttpStatus.OK.value(), null, "소개글이 수정되었습니다.");
            return ResponseEntity.ok(res);
        } catch (DataAccessException dae) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "데이터베이스 오류가 발생했습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 오류가 발생했습니다."));
        }
    }

    @Override
    public ResponseEntity<CustomApiResponse<?>> getIntroduce(Long userId) {
        try {
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(CustomApiResponse.createFailWithout(HttpStatus.BAD_REQUEST.value(), "유저 ID를 제공해야 합니다."));
            }

            Optional<User> findUser = userRepository.findById(userId);

            // 해당 userId를 가진 유저가 쓴 게시물 찾기
            List<Introduce> findIntroduce = introduceRepository.findByUserId(findUser.get());

            // 응답 dto 생성
            List<IntroduceListDto.IntroduceDto> introduceResponse = new ArrayList<>();
            for (Introduce introduce : findIntroduce) {
                introduceResponse.add(IntroduceListDto.IntroduceDto.builder()
                        .introId(introduce.getIntroId())
                        .introContent(introduce.getIntroContent())
                        .availableTime(introduce.getAvailableTime())
                        .strength(introduce.getStrength())
                        .snsId(introduce.getKakaoId())
                        .build());
            }

            // 응답
            IntroduceListDto.SearchIntroducesRes searchIntroduceRes = new IntroduceListDto.SearchIntroducesRes(introduceResponse);
            CustomApiResponse<IntroduceListDto.SearchIntroducesRes> res = CustomApiResponse.createSuccess(HttpStatus.OK.value(), searchIntroduceRes, "소개글 조회 성공");
            return ResponseEntity.ok(res);
        } catch (DataAccessException dae) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "데이터베이스 오류가 발생했습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 오류가 발생했습니다."));
        }
    }
}