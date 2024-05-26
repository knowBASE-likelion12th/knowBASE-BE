package com.knowbase.knowbase.homestylings.controller;

import com.knowbase.knowbase.homestylings.dto.HomestylingCreateDto;
import com.knowbase.knowbase.homestylings.dto.HomestylingDeleteDto;
import com.knowbase.knowbase.homestylings.dto.HomestylingUpdateDto;
import com.knowbase.knowbase.homestylings.service.HomestylingService;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/homestyling")
public class HomestylingController {
    private final HomestylingService homestylingService;

    //홈스타일링 작성
    @PostMapping
    public ResponseEntity<CustomApiResponse<?>> createHomestyling(
            @Valid @RequestBody HomestylingCreateDto.Req homestylingCreateDto) {
        ResponseEntity<CustomApiResponse<?>> homestyling = homestylingService.createHomestyling(homestylingCreateDto);
        return homestyling;
    }

    //홈스타일링 수정
    @PutMapping
    public ResponseEntity<CustomApiResponse<?>> updateHomestyling(
            @RequestParam("homestylingId") Long homestylingId,
            @Valid @RequestBody HomestylingUpdateDto.Req homestylingUpdateDto) {
        ResponseEntity<CustomApiResponse<?>> homestyling = homestylingService.updateHomestyling(homestylingId, homestylingUpdateDto);
        return homestyling;
    }

    //특정 유저의 홈스타일링 조회(모든 유저가 조회 가능)
    @GetMapping("/mentor")
    public ResponseEntity<CustomApiResponse<?>> getHomestyling(
            @RequestParam("userId") Long userId) {
        ResponseEntity<CustomApiResponse<?>> homestyling = homestylingService.getHomestyling(userId);
        return homestyling;
    }

    //홈스타일링 상세 조회
    @GetMapping("/detail")
    public ResponseEntity<CustomApiResponse<?>> getHomestylingDetail(
            @RequestParam("homestylingId") Long homestylingId) {
        ResponseEntity<CustomApiResponse<?>> homestyling = homestylingService.getHomestylingDetail(homestylingId);
        return homestyling;
    }

    //홈스타일링 삭제
    @DeleteMapping
    public ResponseEntity<CustomApiResponse<?>> deleteHomestyling(
            @Valid @RequestBody HomestylingDeleteDto homestylingDeleteDto){
        ResponseEntity<CustomApiResponse<?>> homestyling = homestylingService.deleteHomestyling(homestylingDeleteDto);
        return homestyling;
    }
}
