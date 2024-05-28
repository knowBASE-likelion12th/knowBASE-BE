package com.knowbase.knowbase.homestylings.dto;

import com.knowbase.knowbase.domain.HomeStyling;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter @Builder
@AllArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class HomestylingUpdateDto {
    @NotNull(message = "작성자 id는 비어있을 수 없습니다.")
    private Long userId;

    @NotNull(message = "홈스타일링 제목은 비어있을 수 없습니다.")
    private String homestylingTitle;

    @NotNull(message = "홈스타일링 설명은 비어있을 수 없습니다.")
    private String homestylingDescription;

    @NotNull(message = "홈스타일링 사진은 비어있을 수 없습니다.")
    private MultipartFile homestylingImg;

    public HomeStyling toEntity(String homestylingImagePath){
        return HomeStyling.builder()
                .homeStylingTitle(homestylingTitle)
                .homeStylingDescription(homestylingDescription)
                .homeStylingImagePath(homestylingImagePath)
                .build();
    }
}
