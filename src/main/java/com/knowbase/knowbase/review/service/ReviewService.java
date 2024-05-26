package com.knowbase.knowbase.review.service;

import com.knowbase.knowbase.review.dto.ReviewCreateDto;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import org.springframework.http.ResponseEntity;

public interface ReviewService {
    ResponseEntity<CustomApiResponse<?>> createReview(ReviewCreateDto.Req req);
}
