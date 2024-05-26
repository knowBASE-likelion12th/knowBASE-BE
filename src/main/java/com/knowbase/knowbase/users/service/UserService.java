package com.knowbase.knowbase.users.service;

import com.knowbase.knowbase.users.dto.MenteeSignUpDto;
import com.knowbase.knowbase.users.dto.MentorSignUpDto;
import com.knowbase.knowbase.users.dto.UserSignInDto;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;


public interface UserService {
    ResponseEntity<CustomApiResponse<?>> mentoSignup(MentorSignUpDto mentorSignUpDto);

    ResponseEntity<CustomApiResponse<?>> menteeSignup(MenteeSignUpDto menteeSignUpDto);

    ResponseEntity<CustomApiResponse<?>> signIn(UserSignInDto userSignInDto);

    ResponseEntity<CustomApiResponse<?>> getAllMentors();

    ResponseEntity<CustomApiResponse<?>> getMentorDetail(Long userId);

    ResponseEntity<CustomApiResponse<?>> validateEditAccess(Long loggedInUserId, Long userId);

    ResponseEntity<CustomApiResponse<?>> withdrawMember(Long userId);

    ResponseEntity<CustomApiResponse<?>> logout(HttpSession session);

    // 모든 멘티 조회
    ResponseEntity<CustomApiResponse<?>> getAllMentees();
}
