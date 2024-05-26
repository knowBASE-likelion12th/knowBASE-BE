package com.knowbase.knowbase.review.controller;

import com.knowbase.knowbase.review.dto.ReviewCreateDto;
import com.knowbase.knowbase.review.service.ReviewService;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<CustomApiResponse<?>> createReview(@RequestBody ReviewCreateDto.Req req) {
        return reviewService.createReview(req);
    }

}
