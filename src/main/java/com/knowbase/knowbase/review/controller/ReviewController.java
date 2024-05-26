package com.knowbase.knowbase.review.controller;

import com.knowbase.knowbase.comments.dto.DeleteCommentDto;
import com.knowbase.knowbase.comments.dto.UpdateCommentdto;
import com.knowbase.knowbase.review.dto.ReviewCreateDto;
import com.knowbase.knowbase.review.dto.ReviewUpdateDto;
import com.knowbase.knowbase.review.service.ReviewService;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    //리뷰 작성
    @PostMapping
    public ResponseEntity<CustomApiResponse<?>> createReview(@RequestBody ReviewCreateDto.Req req) {
        return reviewService.createReview(req);
    }

    //리뷰 수정
    @PutMapping
    public ResponseEntity<CustomApiResponse<?>> updateReview(@Valid @RequestBody ReviewUpdateDto.Req reviewUpdateDto) {
        return reviewService.updateReview(reviewUpdateDto);
    }
    //리뷰 삭제
    @DeleteMapping
    public ResponseEntity<CustomApiResponse<?>> deleteReview(@RequestParam("reviewId") Long reviewId){
        return reviewService.deleteReview(reviewId);
    }
    //멘토의 평균 평점 조회
    @GetMapping("/highstar")
    public ResponseEntity<CustomApiResponse<?>> getHighStarAvg(@RequestParam("userId") Long mentorId) {
        return reviewService.getHighStarAvg(mentorId);
    }

    //내가 쓴 후기 조회(멘티)
    @GetMapping("/wrotereview")
    public ResponseEntity<CustomApiResponse<?>> getWroteReview(@RequestParam("userId") Long menteeId) {
        return reviewService.getWroteReview(menteeId);
    }
    //나의 후기 조회(멘토)
    @GetMapping("/myreview")
    public ResponseEntity<CustomApiResponse<?>> getMyReview(@RequestParam("userId") Long mentorId) {
        return reviewService.getMyReview(mentorId);
    }

}
