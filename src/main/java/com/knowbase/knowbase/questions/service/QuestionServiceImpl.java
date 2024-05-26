package com.knowbase.knowbase.questions.service;

import com.knowbase.knowbase.domain.Question;
import com.knowbase.knowbase.domain.User;
import com.knowbase.knowbase.questions.dto.QuestionCreateDto;
import com.knowbase.knowbase.questions.dto.QuestionListDto;
import com.knowbase.knowbase.questions.dto.QuestionUpdateDto;
import com.knowbase.knowbase.questions.repository.QuestionRepository;
import com.knowbase.knowbase.users.repository.UserRepository;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Builder
public class QuestionServiceImpl implements QuestionService{
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;

    //질문 답변 작성
    @Override
    public ResponseEntity<CustomApiResponse<?>> createQuestion(QuestionCreateDto.Req questionCreateDto) {
        //게시글 작성자가 DB에 존재하는지 확인
        Optional<User> findUser = userRepository.findById(questionCreateDto.getUserId());
        if(findUser.isEmpty()){
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.FORBIDDEN.value(),
                            "해당 유저는 존재하지 않습니다."));
        }
        Question newQuestion = questionCreateDto.toEntity();
        newQuestion.createQuestion(findUser.get()); //연관관계 설정
        Question savedQuestion = questionRepository.save(newQuestion);

        //응답 dto
        QuestionCreateDto.CreateQuestion createdQuestionResponse = new QuestionCreateDto.CreateQuestion(savedQuestion.getQuestionId(),savedQuestion.getCreateAt());
        //응답
        CustomApiResponse<QuestionCreateDto.CreateQuestion> res = CustomApiResponse.createSuccess(HttpStatus.OK.value(), createdQuestionResponse,"질문이 작성되었습니다.");
        return ResponseEntity.ok(res);
    }


    //질문 답변 수정
    @Override
    public ResponseEntity<CustomApiResponse<?>> updateQuestion(Long questionId, QuestionUpdateDto.Req questionUpdateDto) {
        //1.수정하려는 질문이 DB에 존재하는지 확인
        Optional<Question> findQuestion = questionRepository.findById(questionId);
        if(findQuestion.isEmpty()){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(CustomApiResponse.createFailWithout(
                            HttpStatus.BAD_REQUEST.value(),
                            "해당 질문은 존재하지 않습니다."));
        }

        //질문 답변 작성자와 수정하려는 유저가 일치하는지 확인
        if(!findQuestion.get().getUserId().getUserId().equals(questionUpdateDto.getUserId())){
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(CustomApiResponse.createFailWithout(
                            HttpStatus.FORBIDDEN.value(),
                            "해당 질문의 작성자만 수정할 수 있습니다."));
        }

        //질문 답변 수정
        Question question = findQuestion.get();
        question.changeQuestionContent1(questionUpdateDto.getQuestionContent1());
        question.changeAnswerContent1(questionUpdateDto.getAnswerContent1());
        question.changeQuestionContent2(questionUpdateDto.getQuestionContent2());
        question.changeAnswerContent2(questionUpdateDto.getAnswerContent2());
        question.changeQuestionContent3(questionUpdateDto.getQuestionContent3());
        question.changeAnswerContent3(questionUpdateDto.getAnswerContent3());
        question.changeQuestionContent4(questionUpdateDto.getQuestionContent4());
        question.changeAnswerContent4(questionUpdateDto.getAnswerContent4());

        //응답 DTO 생성
        QuestionUpdateDto.UpdateQuestion updatedQuestionResponse = QuestionUpdateDto.UpdateQuestion.builder()
                .updateAt(question.getUpdateAt())
                .build();

        //응답
        CustomApiResponse<QuestionUpdateDto.UpdateQuestion> res = CustomApiResponse.createSuccess(HttpStatus.OK.value(), updatedQuestionResponse, "질문이 수정되었습니다.");
        return ResponseEntity.ok(res);
    }

    //특정 유저의 소개글 조회
    @Override
    public ResponseEntity<CustomApiResponse<?>> getQuestion(Long userId) {
        Optional<User> findUser = userRepository.findById(userId);

        //해당 userId를 가진 유저가 존재하지 않을 때
        List<Question> findQuestion = questionRepository.findByUserId(findUser.get());

        //응답 DTO 생성
        List<QuestionListDto.QuestionDto> questionResponse = new ArrayList<>();
        for (Question question : findQuestion) {
            questionResponse.add(QuestionListDto.QuestionDto.builder()
                    .questionId(question.getQuestionId())
                    .questionContent1(question.getQuestionContent1())
                    .answerContent1(question.getAnswerContent1())
                    .questionContent2(question.getQuestionContent2())
                    .answerContent2(question.getAnswerContent2())
                    .questionContent3(question.getQuestionContent3())
                    .answerContent3(question.getAnswerContent3())
                    .questionContent4(question.getQuestionContent4())
                    .answerContent4(question.getAnswerContent4())
                    .updatedAt(question.getUpdateAt())
                    .build());
        }

        //응답
        QuestionListDto.SearchQuestionsRes searchQuestionsRes = new QuestionListDto.SearchQuestionsRes(questionResponse);
        CustomApiResponse<QuestionListDto.SearchQuestionsRes> res = CustomApiResponse.createSuccess(HttpStatus.OK.value(), searchQuestionsRes, "질문이 조회되었습니다.");
        return ResponseEntity.ok(res);
    }
}
