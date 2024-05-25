package com.knowbase.knowbase.introduces.service;

import com.knowbase.knowbase.introduces.dto.IntroduceDto;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import org.springframework.http.ResponseEntity;

public interface IntroduceService {
    ResponseEntity<CustomApiResponse<?>> createIntroduce(Long userId, IntroduceDto introduceDto);
    ResponseEntity<CustomApiResponse<?>> updateIntroduce(Long userId, IntroduceDto introduceDto);
    ResponseEntity<CustomApiResponse<?>> getIntroduceDetail(Long userId);
}
