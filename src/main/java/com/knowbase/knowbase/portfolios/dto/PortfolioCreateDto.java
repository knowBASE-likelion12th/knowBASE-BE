package com.knowbase.knowbase.portfolios.dto;

import com.knowbase.knowbase.domain.Portfolio;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

public class PortfolioCreateDto {
    @Getter @Setter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Req{

        @NotNull(message = "작성자 id는 비어있을 수 없습니다.")
        private Long userId;

        @NotEmpty(message = "포트폴리오 사진 경로는 비어있을 수 없습니다.")
        private String portfolioImagePath;

        public Portfolio toEntity(){
            return Portfolio.builder()
                    .portfolioImagePath(portfolioImagePath)
                    .build();
        }
    }

    @Getter @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CreatePortfolio{
        private Long portfolioId;
        private LocalDateTime createdAt;

        public CreatePortfolio(Long portfolio, LocalDateTime createdAt){
            this.portfolioId = portfolio;
            this.createdAt = createdAt;
        }
    }
}