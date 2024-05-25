package com.knowbase.knowbase.introduces.controller;

import com.knowbase.knowbase.introduces.dto.IntroduceCreateDto;
import com.knowbase.knowbase.introduces.dto.IntroduceUpdateDto;
import com.knowbase.knowbase.introduces.service.IntroduceService;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/introduce")
public class IntroduceController {
    private final IntroduceService introduceService;

    // 소개 글 작성
    @PostMapping
    public ResponseEntity<CustomApiResponse<?>> createIntroduce(
            @Valid @RequestBody IntroduceCreateDto.Req introduceCreateDto) {
        ResponseEntity<CustomApiResponse<?>> introduce = introduceService.createIntroduce(introduceCreateDto);
        return introduce;
    }

    // 소개 글 수정 (인증된 사용자만 가능)
    @PutMapping
    public ResponseEntity<CustomApiResponse<?>> updateIntroduce(
            @RequestParam("introId") Long introId,
            @RequestBody IntroduceUpdateDto.Req introduceUpdateDto) {
        ResponseEntity<CustomApiResponse<?>> introduce = introduceService.updateIntroduce(introId, introduceUpdateDto);
        return introduce;
    }

    // 특정 유저의 소개 글 조회(모든 유저가 조회 가능)
    @GetMapping("/mentor")
    public ResponseEntity<CustomApiResponse<?>> getIntroduce(
            @RequestParam("userId") Long userId) {
        ResponseEntity<CustomApiResponse<?>> introduce = introduceService.getIntroduce(userId);
        return introduce;
    }
}
