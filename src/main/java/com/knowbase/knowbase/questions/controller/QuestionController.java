package com.knowbase.knowbase.questions.controller;

import com.knowbase.knowbase.questions.dto.QuestionCreateDto;
import com.knowbase.knowbase.questions.dto.QuestionUpdateDto;
import com.knowbase.knowbase.questions.service.QuestionService;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/questions")
public class QuestionController {
    private final QuestionService questionService;

    //질문 답변 작성
    @PostMapping
    public ResponseEntity<CustomApiResponse<?>> createQuestion(
            @Valid @RequestBody QuestionCreateDto.Req questionCreateDto) {
        ResponseEntity<CustomApiResponse<?>> question = questionService.createQuestion(questionCreateDto);
        return question;
    }

    //질문 답변 수정
    @PutMapping
    public ResponseEntity<CustomApiResponse<?>> updateQuestion(
            @RequestParam("questionId") Long questionId,
            @Valid @RequestBody QuestionUpdateDto.Req questionUpdateDto) {
        ResponseEntity<CustomApiResponse<?>> question = questionService.updateQuestion(questionId, questionUpdateDto);
        return question;
    }

    //특정 유저의 질문 답변 조회(모든 유저가 조회 가능)
    @GetMapping("/mentor")
    public ResponseEntity<CustomApiResponse<?>> getQuestion(
            @RequestParam("userId") Long userId) {
        ResponseEntity<CustomApiResponse<?>> question = questionService.getQuestion(userId);
        return question;
    }
}
