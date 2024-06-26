package com.knowbase.knowbase.review.service;

import com.knowbase.knowbase.review.dto.ReviewCreateDto;
import com.knowbase.knowbase.review.dto.ReviewUpdateDto;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import org.springframework.http.ResponseEntity;

public interface ReviewService {
    ResponseEntity<CustomApiResponse<?>> createReview(ReviewCreateDto.Req req);

    ResponseEntity<CustomApiResponse<?>> getHighStarAvg(Long mentorId);

    ResponseEntity<CustomApiResponse<?>> getWroteReview(Long menteeId);

    ResponseEntity<CustomApiResponse<?>> getMyReview(Long mentorId);

    ResponseEntity<CustomApiResponse<?>> deleteReview(Long reviewId);

    ResponseEntity<CustomApiResponse<?>> updateReview(ReviewUpdateDto.Req reviewUpdateDto);
}
