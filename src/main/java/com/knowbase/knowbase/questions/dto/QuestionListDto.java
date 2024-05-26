package com.knowbase.knowbase.questions.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class QuestionListDto {

    @Getter @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class QuestionDto {
        private Long questionId;
        private String questionContent1;
        private String answerContent1;
        private String questionContent2;
        private String answerContent2;
        private String questionContent3;
        private String answerContent3;
        private String questionContent4;
        private String answerContent4;
        private LocalDateTime updatedAt;
    }

    //게시물 조회
    @Getter @Setter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SearchQuestionsRes {
        private List<QuestionDto> questions;

        public SearchQuestionsRes(List<QuestionDto> questions) {
            this.questions = questions;
        }
    }
}
