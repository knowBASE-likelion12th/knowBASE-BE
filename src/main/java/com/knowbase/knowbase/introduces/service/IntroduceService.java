package com.knowbase.knowbase.introduces.service;

import com.knowbase.knowbase.introduces.dto.IntroduceCreateDto;
import com.knowbase.knowbase.introduces.dto.IntroduceUpdateDto;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import org.springframework.http.ResponseEntity;

public interface IntroduceService {
    ResponseEntity<CustomApiResponse<?>> createIntroduce(IntroduceCreateDto.Req introduceCreateDto);

    ResponseEntity<CustomApiResponse<?>> updateIntroduce(Long introId, IntroduceUpdateDto.Req introduceUpdateDto);

    ResponseEntity<CustomApiResponse<?>> getIntroduce(Long userId);
}
