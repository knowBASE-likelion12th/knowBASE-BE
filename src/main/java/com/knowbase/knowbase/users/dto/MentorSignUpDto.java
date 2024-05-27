package com.knowbase.knowbase.users.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MentorSignUpDto {
    @NotEmpty(message = "비어있을 수 없습니다.")
    @NotNull(message = "아이디를 입력해주세요.")
    private String userName;

    @NotEmpty(message = "비어있을 수 없습니다.")
    @NotNull(message = "비밀번호를 입력해주세요.")
    private String password;

    @NotEmpty(message = "비어있을 수 없습니다.")
    @NotNull(message = "유저의 이름을 입력해주세요.")
    private String name;

    @NotEmpty(message = "비어있을 수 없습니다.")
    @NotNull(message = "멘토인지 멘티인지 구별해주세요")
    private Boolean isMentor;

    @NotEmpty(message = "비어있을 수 없습니다.")
    @NotNull(message = "닉네임을 입력해주세요")
    private String nickname;

    @NotEmpty(message = "비어있을 수 없습니다.")
    @NotNull(message = "재직증명서를 첨부해주세요.")
    private String employmentPath;

    @NotEmpty(message = "비어있을 수 없습니다.")
    @NotNull(message = "성별을 선택해주세요.")
    private Boolean gender;

    @NotEmpty(message = "비어있을 수 없습니다.")
    @NotNull(message = "연령대를 선택해주세요.")
    private String age;
}