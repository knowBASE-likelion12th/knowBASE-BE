package com.knowbase.knowbase.users.dto;

import lombok.*;

import java.util.List;

public class MenteeListDto {
    @Getter @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class MenteeResponse {
        private Long userId; // 멘티의 기본키
        private String userName; // 멘티의 아이디
        private String nickName; // 멘티의 닉네임
        private String profileImgPath; // 멘티의 프로필 이미지 경로
        private Boolean isMentor; // 멘토인지 멘티인지 구별
    }

    @Getter @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SearchMenteesRes {
        private List<MenteeResponse> mentees;

        public SearchMenteesRes(List<MenteeResponse> mentees) {
            this.mentees = mentees;
        }
    }
}
