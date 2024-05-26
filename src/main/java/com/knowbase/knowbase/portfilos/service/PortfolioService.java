package com.knowbase.knowbase.portfilos.service;

import com.knowbase.knowbase.portfilos.dto.PortfolioCreateDto;
import com.knowbase.knowbase.portfilos.dto.PortfolioDeleteDto;
import com.knowbase.knowbase.portfilos.dto.PortfolioUpdateDto;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import org.springframework.http.ResponseEntity;

public interface PortfolioService {
    ResponseEntity<CustomApiResponse<?>> createPortfolio(PortfolioCreateDto.Req portfolioCreateDto);

    ResponseEntity<CustomApiResponse<?>> updatePortfolio(Long portfolioId, PortfolioUpdateDto.Req portfolioUpdateDto);

    ResponseEntity<CustomApiResponse<?>> getPortfolio(Long userId);

    ResponseEntity<CustomApiResponse<?>> getPortfolioDetail(Long portfolioId);

    ResponseEntity<CustomApiResponse<?>> deletePortfolio(PortfolioDeleteDto portfolioId);
}
