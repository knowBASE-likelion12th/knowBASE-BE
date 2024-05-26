package com.knowbase.knowbase.portfilos.controller;

import com.knowbase.knowbase.portfilos.dto.PortfolioCreateDto;
import com.knowbase.knowbase.portfilos.dto.PortfolioDeleteDto;
import com.knowbase.knowbase.portfilos.dto.PortfolioUpdateDto;
import com.knowbase.knowbase.portfilos.service.PortfolioService;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/portfolio")
public class PortfolioController {
    private final PortfolioService portfolioService;

    //포트폴리오 작성
    @PostMapping
    public ResponseEntity<CustomApiResponse<?>> createPortfolio(
            @Valid @RequestBody PortfolioCreateDto.Req portfolioCreateDto) {
        ResponseEntity<CustomApiResponse<?>> portfolio = portfolioService.createPortfolio(portfolioCreateDto);
        return portfolio;
    }

    //포트폴리오 수정
    @PutMapping
    public ResponseEntity<CustomApiResponse<?>> updatePortfolio(
            @RequestParam("portfolioId") Long portfolioId,
            @Valid @RequestBody PortfolioUpdateDto.Req portfolioUpdateDto) {
        ResponseEntity<CustomApiResponse<?>> portfolio = portfolioService.updatePortfolio(portfolioId, portfolioUpdateDto);
        return portfolio;
    }

    //특정 유저의 포트폴리오 조회(모든 유저가 조회 가능)
    @GetMapping("/mentor")
    public ResponseEntity<CustomApiResponse<?>> getPortfolio(
            @RequestParam("userId") Long userId) {
        ResponseEntity<CustomApiResponse<?>> portfolio = portfolioService.getPortfolio(userId);
        return portfolio;
    }

    //포트폴리오 상세 조회
    @GetMapping("/detail")
    public ResponseEntity<CustomApiResponse<?>> getPortfolioDetail(
            @RequestParam("portfolioId") Long portfolioId) {
        ResponseEntity<CustomApiResponse<?>> portfolio = portfolioService.getPortfolioDetail(portfolioId);
        return portfolio;
    }

    //포트폴리오 삭제
    @DeleteMapping
    public ResponseEntity<CustomApiResponse<?>> deletePortfolio(
            @Valid @RequestBody PortfolioDeleteDto portfolioDeleteDto){
        ResponseEntity<CustomApiResponse<?>> portfolio = portfolioService.deletePortfolio(portfolioDeleteDto);
        return portfolio;
    }
}
