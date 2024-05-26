package com.knowbase.knowbase.homestylings.service;

import com.knowbase.knowbase.domain.HomeStyling;
import com.knowbase.knowbase.domain.User;
import com.knowbase.knowbase.homestylings.dto.HomeStylingListDto;
import com.knowbase.knowbase.homestylings.dto.HomestylingCreateDto;
import com.knowbase.knowbase.homestylings.dto.HomestylingDeleteDto;
import com.knowbase.knowbase.homestylings.dto.HomestylingUpdateDto;
import com.knowbase.knowbase.homestylings.repository.HomestylingRepository;
import com.knowbase.knowbase.users.repository.UserRepository;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Builder
public class HomestylingServiceImpl implements HomestylingService {
    private final HomestylingRepository homestylingRepository;
    private final UserRepository userRepository;

    //홈스타일링 작성
    @Override
    public ResponseEntity<CustomApiResponse<?>> createHomestyling(HomestylingCreateDto.Req homestylingCreateDto) {

        //게시글 작성자가 DB에 존재하는지 확인
        Optional<User> findUser = userRepository.findById(homestylingCreateDto.getUserId());
        if (findUser.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.FORBIDDEN.value(),
                            "해당 유저는 존재하지 않습니다."));
        }
        HomeStyling newHomestyling = homestylingCreateDto.toEntity();
        newHomestyling.createHomeStyling(findUser.get()); //연관관계 설정
        HomeStyling savedHomestyling = homestylingRepository.save(newHomestyling);

        //응답 dto
        HomestylingCreateDto.CreateHomestyling createdHomestylingResponse = new HomestylingCreateDto.CreateHomestyling(savedHomestyling.getHomeStylingId(), savedHomestyling.getCreateAt());
        //응답
        CustomApiResponse<HomestylingCreateDto.CreateHomestyling> res = CustomApiResponse.createSuccess(HttpStatus.OK.value(), createdHomestylingResponse, "홈스타일링이 작성되었습니다.");
        return ResponseEntity.ok(res);
    }

    //홈스타일링 수정
    @Override
    public ResponseEntity<CustomApiResponse<?>> updateHomestyling(Long homestylingId, HomestylingUpdateDto.Req homestylingUpdateDto) {
        //1.수정하려는 홈스타일링이 DB에 존재하는지 확인
        Optional<HomeStyling> findHomestyling = homestylingRepository.findById(homestylingId);
        if (findHomestyling.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(CustomApiResponse.createFailWithout(
                            HttpStatus.BAD_REQUEST.value(),
                            "해당 홈스타일링이 존재하지 않습니다."));
        }

        //2.수정하려는 홈스타일링이 로그인한 유저의 것인지 확인
        if (!findHomestyling.get().getUserId().getUserId().equals(homestylingUpdateDto.getUserId())) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(CustomApiResponse.createFailWithout(
                            HttpStatus.FORBIDDEN.value(),
                            "해당 홈스타일링은 수정할 수 없습니다."));
        }

        //3.홈스타일링 수정
        HomeStyling homestyling = findHomestyling.get();
        homestyling.changeTitle(homestylingUpdateDto.getHomestylingTitle());
        homestyling.changeDescription(homestylingUpdateDto.getHomestylingDescription());
        homestyling.changeImagePath(homestylingUpdateDto.getHomestylingImagePath());

        //응답 DTO 생성
        HomestylingUpdateDto.UpdateHomestyling updatedHomestylingResponse = HomestylingUpdateDto.UpdateHomestyling.builder()
                .updatedAt(homestyling.getUpdateAt())
                .build();

        //응답
        CustomApiResponse<HomestylingUpdateDto.UpdateHomestyling> res = CustomApiResponse.createSuccess(HttpStatus.OK.value(), updatedHomestylingResponse, "홈스타일링이 수정되었습니다.");
        return ResponseEntity.ok(res);
    }

    //특정 유저의 홈스타일링 조회(모든 유저가 조회 가능)
    @Override
    public ResponseEntity<CustomApiResponse<?>> getHomestyling(Long userId) {
        //1.조회하려는 유저가 DB에 존재하는지 확인
        Optional<User> findUser = userRepository.findById(userId);

        //2.해당 유저의 홈스타일링 조회
        List<HomeStyling> findHomestyling = homestylingRepository.findByUserId(findUser.get());

        //응답 DTO 생성
        List<HomeStylingListDto.HomeStyling> homestylingList = new ArrayList<>();
        for(HomeStyling homestyling : findHomestyling){
            HomeStylingListDto.HomeStyling homestylingDto = HomeStylingListDto.HomeStyling.builder()
                    .homestylingId(homestyling.getHomeStylingId())
                    .homestylingTitle(homestyling.getHomeStylingTitle())
                    .homestylingImagePath(homestyling.getHomeStylingImagePath())
                    .homestylingDescription(homestyling.getHomeStylingDescription())
                    .build();
            homestylingList.add(homestylingDto);
        }

        //응답
        HomeStylingListDto.SearchHomestylingsRes searchHomeStylingRes = new HomeStylingListDto.SearchHomestylingsRes(homestylingList);
        CustomApiResponse<HomeStylingListDto.SearchHomestylingsRes> res = CustomApiResponse.createSuccess(HttpStatus.OK.value(), searchHomeStylingRes, "홈스타일링이 조회되었습니다.");
        return ResponseEntity.ok(res);
    }

    //특정 홈스타일링 조회
    @Override
    public ResponseEntity<CustomApiResponse<?>> getHomestylingDetail(Long homestylingId) {
        //1.조회하려는 홈스타일링이 DB에 존재하는지 확인
        Optional<HomeStyling> findHomestyling = homestylingRepository.findById(homestylingId);
        if (findHomestyling.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(CustomApiResponse.createFailWithout(
                            HttpStatus.BAD_REQUEST.value(),
                            "해당 홈스타일링이 존재하지 않습니다."));
        }

        //응답 DTO 생성
        HomeStylingListDto.HomeStyling homestyling = HomeStylingListDto.HomeStyling.builder()
                .homestylingId(findHomestyling.get().getHomeStylingId())
                .homestylingTitle(findHomestyling.get().getHomeStylingTitle())
                .homestylingImagePath(findHomestyling.get().getHomeStylingImagePath())
                .homestylingDescription(findHomestyling.get().getHomeStylingDescription())
                .updatedAt(findHomestyling.get().getUpdateAt())
                .build();

        //응답
        CustomApiResponse<HomeStylingListDto.HomeStyling> res = CustomApiResponse.createSuccess(HttpStatus.OK.value(), homestyling, "홈스타일링이 조회되었습니다.");
        return ResponseEntity.ok(res);
    }

    //홈스타일링 삭제
    @Override
    public ResponseEntity<CustomApiResponse<?>> deleteHomestyling(HomestylingDeleteDto homestylingDeleteDto) {
        //1.삭제하려는 홈스타일링이 DB에 존재하는지 확인
        Optional<HomeStyling> findHomestyling = homestylingRepository.findById(homestylingDeleteDto.getHomestylingId());
        if (findHomestyling.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(CustomApiResponse.createFailWithout(
                            HttpStatus.BAD_REQUEST.value(),
                            "해당 홈스타일링이 존재하지 않습니다."));
        }

        //현재 접속한 사용자와 홈스타일링 작성자가 같은지 확인
        if (!findHomestyling.get().getUserId().getUserId().equals(homestylingDeleteDto.getUserId())) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(CustomApiResponse.createFailWithout(
                            HttpStatus.FORBIDDEN.value(),
                            "해당 홈스타일링은 삭제할 수 없습니다."));
        }

        //홈스타일링 삭제
        homestylingRepository.delete(findHomestyling.get());

        //응답
        CustomApiResponse<?> res = CustomApiResponse.createSuccess(HttpStatus.OK.value(), null, "홈스타일링이 삭제되었습니다.");
        return ResponseEntity.ok(res);
    }
}