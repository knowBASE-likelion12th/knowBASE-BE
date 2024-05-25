package com.knowbase.knowbase.introduces.service;

import com.knowbase.knowbase.domain.Introduce;
import com.knowbase.knowbase.domain.User;
import com.knowbase.knowbase.introduces.dto.IntroduceDto;
import com.knowbase.knowbase.introduces.repository.IntroduceRepository;
import com.knowbase.knowbase.users.repository.UserRepository;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IntroduceServiceImpl implements com.knowbase.knowbase.introduces.service.IntroduceService {
    private final IntroduceRepository introduceRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseEntity<CustomApiResponse<?>> createIntroduce(Long userId, IntroduceDto introduceDto) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "사용자를 찾을 수 없습니다."));
        }

        Introduce introduce = Introduce.builder()
                .userId(user.get())
                .introContent(introduceDto.getIntroContent())
                .availableTime(introduceDto.getAvailableTime())
                .strength(introduceDto.getStrength())
                .build();

        introduceRepository.save(introduce);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CustomApiResponse.createSuccess(HttpStatus.CREATED.value(), null, "소개 글이 성공적으로 작성되었습니다."));
    }

    @Override
    public ResponseEntity<CustomApiResponse<?>> updateIntroduce(Long introId, IntroduceDto introduceDto) {
        Optional<Introduce> introduce = introduceRepository.findById(introId);
        if (introduce.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "소개 글을 찾을 수 없습니다."));
        }

        Introduce existingIntroduce = introduce.get();
        existingIntroduce.changeIntroContent(introduceDto.getIntroContent());
        existingIntroduce.changeAvailableTime(introduceDto.getAvailableTime());
        existingIntroduce.changeStrength(introduceDto.getStrength());

        introduceRepository.save(existingIntroduce);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CustomApiResponse.createSuccess(HttpStatus.OK.value(), null, "소개 글이 성공적으로 수정되었습니다."));
    }

    @Override
    public ResponseEntity<CustomApiResponse<?>> getIntroduceDetail(Long introId) {
        Optional<Introduce> introduce = introduceRepository.findById(introId);
        if (introduce.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "소개 글을 찾을 수 없습니다."));
        }

        Introduce existingIntroduce = introduce.get();
        IntroduceDto introduceDto = IntroduceDto.builder()
                .introContent(existingIntroduce.getIntroContent())
                .availableTime(existingIntroduce.getAvailableTime())
                .strength(existingIntroduce.getStrength())
                .userId(existingIntroduce.getUserId().getUserId())
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .body(CustomApiResponse.createSuccess(HttpStatus.OK.value(), introduceDto, "소개 글 조회에 성공하였습니다."));
    }
}
