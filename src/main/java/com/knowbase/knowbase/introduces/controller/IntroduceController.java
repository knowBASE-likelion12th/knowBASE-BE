package com.knowbase.knowbase.introduces.controller;
import com.knowbase.knowbase.introduces.dto.IntroduceDto;
import com.knowbase.knowbase.introduces.service.IntroduceService;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/introduce")
public class IntroduceController {
    private final IntroduceService introduceService;

    // 소개 글 작성
    @PostMapping("/create")
    public ResponseEntity<CustomApiResponse<?>> createIntroduce(
            @RequestParam("userId") Long userId,
            @RequestBody IntroduceDto introduceDto) {
        return introduceService.createIntroduce(userId, introduceDto);
    }

    // 소개 글 수정
    @PutMapping("/update/{introId}")
    public ResponseEntity<CustomApiResponse<?>> updateIntroduce(
            @PathVariable("introId") Long introId,
            @RequestBody IntroduceDto introduceDto) {
        return introduceService.updateIntroduce(introId, introduceDto);
    }

    // 소개 글 조회
    @GetMapping("/{introId}")
    public ResponseEntity<CustomApiResponse<?>> getIntroduceDetail(
            @PathVariable("introId") Long introId) {
        return introduceService.getIntroduceDetail(introId);
    }
}
