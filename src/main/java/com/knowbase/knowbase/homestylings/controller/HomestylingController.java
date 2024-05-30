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
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/homeStyle")
public class HomestylingController {
    private final HomestylingService homestylingService;

    // 홈스타일링 작성
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<CustomApiResponse<?>> createHomestyling(
            @RequestPart("homestylingCreateDto") HomestylingCreateDto.HomestylingCreateDtoReq homestylingCreateDtoReq,
            @RequestPart("homestylingImg") MultipartFile homestylingImg) {
        ResponseEntity<CustomApiResponse<?>> homestyling = homestylingService.createHomestyling(homestylingCreateDtoReq, homestylingImg);
        return homestyling;
    }

    // 홈스타일링 수정
    @PatchMapping(value = "/{homestylingId}", consumes = {"multipart/form-data"})
    public ResponseEntity<CustomApiResponse<?>> updateHomestyling(
            @PathVariable("homestylingId") Long homestylingId,
            @Valid @RequestPart("homestylingUpdateDto") HomestylingUpdateDto homestylingUpdateDto,
            @RequestPart("homestylingImg") MultipartFile homestylingImg) {
        return homestylingService.updateHomestyling(homestylingId, homestylingUpdateDto, homestylingImg);
    }

    //특정 유저의 홈스타일링 조회(모든 유저가 조회 가능)
    @GetMapping
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
