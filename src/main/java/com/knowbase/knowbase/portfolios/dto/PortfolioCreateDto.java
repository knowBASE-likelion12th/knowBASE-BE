package com.knowbase.knowbase.portfolios.dto;

import com.knowbase.knowbase.domain.Portfolio;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PortfolioCreateDto {
    @NotNull(message = "작성자 id는 비어있을 수 없습니다.")
    private Long userId;

    @NotNull(message = "포트폴리오 사진은 비어있을 수 없습니다.")
    private MultipartFile portfolioImg;

    public Portfolio toEntity(String portfolioImagePath) {
        return Portfolio.builder()
                .portfolioImagePath(portfolioImagePath)
                .build();
    }
}
