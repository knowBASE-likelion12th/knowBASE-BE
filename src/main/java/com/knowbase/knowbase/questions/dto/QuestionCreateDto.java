package com.knowbase.knowbase.questions.dto;

import com.knowbase.knowbase.domain.Question;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

public class QuestionCreateDto {
    @Getter @Setter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Req{
        @NotNull(message = "작성자 id는 비어있을 수 없습니다.")
        private Long userId;

        @NotEmpty(message = "질문 내용은 비어있을 수 없습니다.")
        private String questionContent1;

        @NotEmpty(message = "답변 내용은 비어있을 수 없습니다.")
        private String answerContent1;

        @NotEmpty(message = "질문 내용은 비어있을 수 없습니다.")
        private String questionContent2;

        @NotEmpty(message = "답변 내용은 비어있을 수 없습니다.")
        private String answerContent2;

        @NotEmpty(message = "질문 내용은 비어있을 수 없습니다.")
        private String questionContent3;

        @NotEmpty(message = "답변 내용은 비어있을 수 없습니다.")
        private String answerContent3;

        @NotEmpty(message = "질문 내용은 비어있을 수 없습니다.")
        private String questionContent4;

        @NotEmpty(message = "답변 내용은 비어있을 수 없습니다.")
        private String answerContent4;

        public Question toEntity(){
            return Question.builder()
                    .questionContent1(questionContent1)
                    .answerContent1(answerContent1)
                    .answerContent2(answerContent2)
                    .questionContent2(questionContent2)
                    .answerContent3(answerContent3)
                    .questionContent3(questionContent3)
                    .answerContent4(answerContent4)
                    .questionContent4(questionContent4)
                    .build();
        }
    }
}
