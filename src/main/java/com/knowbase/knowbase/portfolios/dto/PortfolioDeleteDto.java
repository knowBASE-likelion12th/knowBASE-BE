package com.knowbase.knowbase.portfolios.dto;


import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class PortfolioDeleteDto {
    @NotNull(message = "작성자 id는 비어있을 수 없습니다.")
    private Long userId;

    @NotNull(message = "포트폴리오의 기본키가 필요합니다.")
    private Long portfolioId;
}
