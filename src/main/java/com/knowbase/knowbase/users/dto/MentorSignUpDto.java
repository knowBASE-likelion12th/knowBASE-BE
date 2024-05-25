package com.knowbase.knowbase.users.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MentorSignUpDto {
    @NotNull(message = "아이디를 입력해주세요.")
    private String userName;

    @NotNull(message = "비밀번호를 입력해주세요.")
    private String password;

    @NotNull(message = "유저의 이름을 입력해주세요.")
    private String name;

    @NotNull(message = "멘토인지 멘티인지 구별해주세요")
    private Boolean isMentor;

    @NotNull(message = "닉네임을 입력해주세요")
    private String nickname;

    @NotNull(message = "재직증명서를 첨부해주세요.")
    private String employmentPath;

    @NotNull(message = "성별을 선택해주세요.")
    private Boolean gender;

    @NotNull(message = "연령대를 선택해주세요.")
    private String age;
}