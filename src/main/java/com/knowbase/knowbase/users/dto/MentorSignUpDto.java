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

    @NotNull(message = "재직증명서를 첨부해주세요.")
    private String employmentPath;

    private String profileImgPath;
    private String mentoringPath;
    private String mentorContent;
}