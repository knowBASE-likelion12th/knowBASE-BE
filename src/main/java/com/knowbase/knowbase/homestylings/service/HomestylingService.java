package com.knowbase.knowbase.homestylings.service;

import com.knowbase.knowbase.homestylings.dto.HomestylingCreateDto;
import com.knowbase.knowbase.homestylings.dto.HomestylingDeleteDto;
import com.knowbase.knowbase.homestylings.dto.HomestylingUpdateDto;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface HomestylingService {
    ResponseEntity<CustomApiResponse<?>> updateHomestyling(Long homestylingId, HomestylingUpdateDto homestylingUpdateDto, MultipartFile homestylingImg);

    ResponseEntity<CustomApiResponse<?>> getHomestyling(Long userId);

    ResponseEntity<CustomApiResponse<?>> getHomestylingDetail(Long homestylingId);

    ResponseEntity<CustomApiResponse<?>> deleteHomestyling(HomestylingDeleteDto homestylingId);

    ResponseEntity<CustomApiResponse<?>> createHomestyling(HomestylingCreateDto.HomestylingCreateDtoReq homestylingCreateDtoReq, MultipartFile homestylingImg);
}
