package com.knowbase.knowbase.portfolios.service;

import com.knowbase.knowbase.portfolios.dto.PortfolioCreateDto;
import com.knowbase.knowbase.portfolios.dto.PortfolioDeleteDto;
import com.knowbase.knowbase.portfolios.dto.PortfolioUpdateDto;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import org.springframework.http.ResponseEntity;

public interface PortfolioService {
    ResponseEntity<CustomApiResponse<?>> createPortfolio(PortfolioCreateDto portfolioCreateDto);

    ResponseEntity<CustomApiResponse<?>> updatePortfolio(Long portfolioId, PortfolioUpdateDto portfolioUpdateDto);

    ResponseEntity<CustomApiResponse<?>> getPortfolio(Long userId);

    ResponseEntity<CustomApiResponse<?>> getPortfolioDetail(Long portfolioId);

    ResponseEntity<CustomApiResponse<?>> deletePortfolio(PortfolioDeleteDto portfolioDeleteDto);
}
