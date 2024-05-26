package com.knowbase.knowbase.homestylings.service;

import com.knowbase.knowbase.homestylings.dto.HomestylingCreateDto;
import com.knowbase.knowbase.homestylings.dto.HomestylingDeleteDto;
import com.knowbase.knowbase.homestylings.dto.HomestylingUpdateDto;
import com.knowbase.knowbase.portfilos.dto.PortfolioCreateDto;
import com.knowbase.knowbase.portfilos.dto.PortfolioDeleteDto;
import com.knowbase.knowbase.portfilos.dto.PortfolioUpdateDto;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import org.springframework.http.ResponseEntity;

public interface HomestylingService {
    ResponseEntity<CustomApiResponse<?>> updateHomestyling(Long homestylingId, HomestylingUpdateDto.Req homestylingUpdateDto);

    ResponseEntity<CustomApiResponse<?>> getHomestyling(Long userId);

    ResponseEntity<CustomApiResponse<?>> getHomestylingDetail(Long homestylingId);

    ResponseEntity<CustomApiResponse<?>> deleteHomestyling(HomestylingDeleteDto homestylingId);

    //홈스타일링 작성
    ResponseEntity<CustomApiResponse<?>> createHomestyling(HomestylingCreateDto.Req homestylingCreateDto);
}
