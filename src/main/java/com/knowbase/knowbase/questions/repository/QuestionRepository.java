package com.knowbase.knowbase.questions.repository;

import com.knowbase.knowbase.domain.Question;
import com.knowbase.knowbase.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByUserId(User userId);
}
