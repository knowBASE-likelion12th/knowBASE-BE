package com.knowbase.knowbase.domain;

import com.knowbase.knowbase.util.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity @Getter @Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
        name = "seq_question",
        sequenceName = "seq_question",
        initialValue = 1,
        allocationSize = 1
)
@Table(name = "questions")
public class Question extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_question")
    @Column(name = "question_id")
    private Long questionId;

    //한명의 유저는 4개의 질문과 답변을 작성할 수 있다.
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User userId;

    //질문 내용
    @Column(name = "QUESTION_CONTENT1")
    private String questionContent1;

    //답변 내용
    @Column(name = "ANSWER_CONTENT1")
    private String answerContent1;

    //질문 내용
    @Column(name = "QUESTION_CONTENT2")
    private String questionContent2;

    //답변 내용
    @Column(name = "ANSWER_CONTENT2")
    private String answerContent2;

    //질문 내용
    @Column(name = "QUESTION_CONTENT3")
    private String questionContent3;

    //답변 내용
    @Column(name = "ANSWER_CONTENT3")
    private String answerContent3;

    //질문 내용
    @Column(name = "QUESTION_CONTENT4")
    private String questionContent4;

    //답변 내용
    @Column(name = "ANSWER_CONTENT4")
    private String answerContent4;

    //연관관계 편의 메소드
    //회원에 대한 연관관계 설정
    public void createQuestion(User userId) {
        this.userId = userId;
    }

    //질문 내용 수정 메소드
    public void changeQuestionContent1(String questionContent1) {
        this.questionContent1 = questionContent1;
    }

    //답변 내용 수정 메소드
    public void changeAnswerContent1(String answerContent1) {
        this.answerContent1 = answerContent1;
    }

    //질문 내용 수정 메소드
    public void changeQuestionContent2(String questionContent2) {
        this.questionContent2 = questionContent2;
    }

    //답변 내용 수정 메소드
    public void changeAnswerContent2(String answerContent2) {
        this.answerContent2 = answerContent2;
    }

    //질문 내용 수정 메소드
    public void changeQuestionContent3(String questionContent3) {
        this.questionContent3 = questionContent3;
    }

    //답변 내용 수정 메소드
    public void changeAnswerContent3(String answerContent3) {
        this.answerContent3 = answerContent3;
    }

    //질문 내용 수정 메소드
    public void changeQuestionContent4(String questionContent4) {
        this.questionContent4 = questionContent4;
    }

    //답변 내용 수정 메소드
    public void changeAnswerContent4(String answerContent4) {
        this.answerContent4 = answerContent4;
    }
}
