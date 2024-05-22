package com.knowbase.knowbase.users.service;

import com.knowbase.knowbase.users.dto.UserSignUpDto;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


public interface UserService {
    ResponseEntity<CustomApiResponse<?>> mentoSignup(UserSignUpDto memberSignUpDto);

}
