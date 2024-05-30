package com.knowbase.knowbase.portfolios.service;

import com.knowbase.knowbase.portfolios.dto.PortfolioCreateDto;
import com.knowbase.knowbase.portfolios.dto.PortfolioDeleteDto;
import com.knowbase.knowbase.portfolios.dto.PortfolioUpdateDto;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface PortfolioService {
    ResponseEntity<CustomApiResponse<?>> createPortfolio(PortfolioCreateDto portfolioCreateDto, MultipartFile portfolioImg);

    ResponseEntity<CustomApiResponse<?>> updatePortfolio(Long portfolioId, PortfolioUpdateDto portfolioUpdateDto, MultipartFile portfolioImg);

    ResponseEntity<CustomApiResponse<?>> getPortfolio(Long userId);

    ResponseEntity<CustomApiResponse<?>> getPortfolioDetail(Long portfolioId);

    ResponseEntity<CustomApiResponse<?>> deletePortfolio(PortfolioDeleteDto portfolioDeleteDto);
}
