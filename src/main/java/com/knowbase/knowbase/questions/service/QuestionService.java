package com.knowbase.knowbase.questions.service;

import com.knowbase.knowbase.questions.dto.QuestionCreateDto;
import com.knowbase.knowbase.questions.dto.QuestionUpdateDto;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import org.springframework.http.ResponseEntity;

public interface QuestionService {
    ResponseEntity<CustomApiResponse<?>> createQuestion(QuestionCreateDto.Req questionCreateDto);

    ResponseEntity<CustomApiResponse<?>> updateQuestion(Long questionId, QuestionUpdateDto.Req questionUpdateDto);

    ResponseEntity<CustomApiResponse<?>> getQuestion(Long userId);
}
