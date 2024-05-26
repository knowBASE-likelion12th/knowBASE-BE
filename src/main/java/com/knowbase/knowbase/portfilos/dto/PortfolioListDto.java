package com.knowbase.knowbase.portfilos.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class PortfolioListDto {

    @Getter @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PortfolioDto {
        private Long portfolioId;
        private String portfolioImagePath;
        private LocalDateTime updatedAt;
    }

    //포트폴리오 조회
    @Getter @Setter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SearchPortfoliosRes {
        private List<PortfolioDto> portfolios;

        public SearchPortfoliosRes(List<PortfolioDto> portfolios) {
            this.portfolios = portfolios;
        }
    }
}
