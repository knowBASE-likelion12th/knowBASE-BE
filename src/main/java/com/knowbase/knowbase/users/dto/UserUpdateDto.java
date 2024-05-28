package com.knowbase.knowbase.users.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserUpdateDto {
    @NotBlank(message = "유저 닉네임은 필수입니다")
    private String nickName;

    @NotBlank(message = "유저 아이디는 필수입니다.")
    private String userName;

    private MultipartFile profileImg;
    private MultipartFile mentoringImg;

    private String mentorContent;
}
