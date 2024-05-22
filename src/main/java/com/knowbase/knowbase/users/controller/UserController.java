package com.knowbase.knowbase.users.controller;

import com.knowbase.knowbase.users.dto.UserSignUpDto;
import com.knowbase.knowbase.users.service.UserService;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;

    //멘토 회원가입
    @PostMapping("/register/mentor")
    private ResponseEntity<CustomApiResponse<?>> mentoSignup(@Valid @RequestBody UserSignUpDto memberSignUpDto ){
        return userService.mentoSignup(memberSignUpDto);
    }


    //멘티 회원가입

    //로그인

    //탈퇴

}
