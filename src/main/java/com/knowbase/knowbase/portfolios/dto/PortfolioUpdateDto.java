package com.knowbase.knowbase.portfolios.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.knowbase.knowbase.domain.Portfolio;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter @Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class PortfolioUpdateDto {
    @NotNull(message = "작성자 id는 비어있을 수 없습니다.")
    private Long userId;
}
