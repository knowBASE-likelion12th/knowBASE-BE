package com.knowbase.knowbase.users.dto;

import lombok.*;

import java.util.List;

public class MentorListDto {
    @Getter @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class MentorResponse{
        private Long userId; // 멘토의 기본키
        private String userName; // 멘토의 아이디
        private String nickName; // 멘토의 닉네임
        private String profileImgPath; // 멘토의 프로필 이미지 경로
        private String mentorContent;  // 멘토의 멘토링 소개글
        private String mentoringPath;  // 멘토의 멘토링 경로
        private Boolean isMentor; // 멘토인지 멘티인지 구별
        private String KakaoId; // 멘토의 카카오 아이디
    }

    //멘토 리스트 조회 : List<Mentor> mentors
    @Getter @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SearchMentorsRes{
        private List<MentorResponse> mentors;

        public SearchMentorsRes(List<MentorResponse> mentors){
            this.mentors = mentors;
        }
    }
}
