package com.knowbase.knowbase.users.service;

import com.knowbase.knowbase.users.dto.MenteeSignUpDto;
import com.knowbase.knowbase.users.dto.MentorSignUpDto;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import org.springframework.http.ResponseEntity;


public interface UserService {
    ResponseEntity<CustomApiResponse<?>> mentoSignup(MentorSignUpDto memberSignUpDto);

    ResponseEntity<CustomApiResponse<?>> menteeSignup(MenteeSignUpDto menteeSignUpDto);
}
