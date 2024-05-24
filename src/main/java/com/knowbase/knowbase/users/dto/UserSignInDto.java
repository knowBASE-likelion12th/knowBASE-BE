package com.knowbase.knowbase.users.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class UserSignInDto {
    @NotEmpty(message = "아이디를 작성 해주세요")
    private String userName;
    @NotEmpty(message = "비밀번호를 작성 해주세요")
    private String password;


    @Getter
    @NoArgsConstructor
    public static class AccountEnter {
        private Long userId;
        private String password;
        private Boolean isMentor;

        public AccountEnter(Long userId,String password,Boolean isMentor) {
            this.password = password;
            this.userId = userId;
            this.isMentor = isMentor;
        }
    }

}
