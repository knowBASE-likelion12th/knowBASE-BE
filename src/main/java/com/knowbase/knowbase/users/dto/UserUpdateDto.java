package com.knowbase.knowbase.users.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter @Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserUpdateDto {
    @NotNull(message = "작성자 ID은 필수입니다")
    private Long userId;

    @NotNull(message = "유저 닉네임은 필수입니다")
    private String nickName;

    @NotNull(message = "유저 아이디는 필수입니다.")
    private String userName;

    @NotNull(message = "멘토링 설명은 필수입니다.")
    private String mentorContent;
}
