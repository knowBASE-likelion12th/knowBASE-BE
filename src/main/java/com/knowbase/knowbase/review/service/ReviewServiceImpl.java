package com.knowbase.knowbase.review.service;

import com.knowbase.knowbase.review.dto.ReviewCreateDto;
import com.knowbase.knowbase.review.repository.ReviewRepository;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{
    private final ReviewRepository reviewRepository;

    //후기 작성
    @Override
    public ResponseEntity<CustomApiResponse<?>> createReview(ReviewCreateDto.Req req) {
        return null;
    }
}
