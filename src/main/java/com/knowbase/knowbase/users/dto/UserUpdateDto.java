package com.knowbase.knowbase.users.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserUpdateDto {
    @NotBlank(message = "User name is required")
    private String userName;

    @NotBlank(message = "Nick name is required")
    private String nickName;

    private String profileImgPath;
}
