package com.knowbase.knowbase.users.controller;

import com.knowbase.knowbase.users.dto.MenteeSignUpDto;
import com.knowbase.knowbase.users.dto.MentorSignUpDto;
import com.knowbase.knowbase.users.dto.UserSignInDto;
import com.knowbase.knowbase.users.service.UserService;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user")
public class UserController {
    private final UserService userService;

    //멘토 회원가입
    @PostMapping("/register/mentor")
    private ResponseEntity<CustomApiResponse<?>> mentoSignup(@Valid @RequestBody MentorSignUpDto memberSignUpDto ){
        return userService.mentoSignup(memberSignUpDto);
    }

    //멘티 회원가입
    @PostMapping("/register/mentee")
    private ResponseEntity<CustomApiResponse<?>> menteeSignup(@Valid @RequestBody MenteeSignUpDto menteeSignUpDto ){
        return userService.menteeSignup(menteeSignUpDto);
    }
    //로그인
    @PostMapping("/login")
    private ResponseEntity<CustomApiResponse<?>> signIn(@Valid @RequestBody UserSignInDto userSignInDto){
        return userService.signIn(userSignInDto);
    }

    //로그아웃

    //탈퇴


    //멘토(isMentor=true) 모두 조회
    @GetMapping("/all")
    private ResponseEntity<CustomApiResponse<?>> getAllMentors(){
        ResponseEntity<CustomApiResponse<?>> result =  userService.getAllMentors();
        return result;
    }

    //멘토 상세 조회
    @GetMapping()
    private ResponseEntity<CustomApiResponse<?>> getMentorDetail(
            @RequestParam("userId") Long userId){
        System.out.println("LOG userId " + userId);
        ResponseEntity<CustomApiResponse<?>> result = userService.getMentorDetail(userId);
        return result;
    }
}
