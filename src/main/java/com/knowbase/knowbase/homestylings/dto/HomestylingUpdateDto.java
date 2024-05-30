package com.knowbase.knowbase.homestylings.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter @Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HomestylingUpdateDto {

    @NotNull(message = "작성자 id는 비어있을 수 없습니다.")
    private Long userId;

    @NotNull(message="홈스타일링 제목은 비어있을 수 없습니다.")
    private String homestylingTitle;

    @NotNull(message = "홈스타일링 설명은 비어있을 수 없습니다.")
    private String homestylingDescription;
}
