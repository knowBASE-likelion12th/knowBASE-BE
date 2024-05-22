package com.knowbase.knowbase.users.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserSignInDto {
    @NotEmpty(message = "아이디를 작성 해주세요")
    private String userName;
    @NotEmpty(message = "비밀번호를 작성 해주세요")
    private String password;
}
