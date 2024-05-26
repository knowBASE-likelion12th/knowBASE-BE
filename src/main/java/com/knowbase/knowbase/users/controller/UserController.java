package com.knowbase.knowbase.users.controller;

import com.knowbase.knowbase.users.dto.MenteeSignUpDto;
import com.knowbase.knowbase.users.dto.MentorSignUpDto;
import com.knowbase.knowbase.users.dto.UserSignInDto;
import com.knowbase.knowbase.users.service.UserService;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user")
public class UserController {
    private final UserService userService;

    //멘토 회원가입
    @PostMapping("/register/mentor")
    private ResponseEntity<CustomApiResponse<?>> mentoSignup(@Valid @RequestBody MentorSignUpDto mentorSignUpDto ){
        return userService.mentoSignup(mentorSignUpDto);
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
    @PostMapping("/logout")
    private ResponseEntity<CustomApiResponse<?>> logout(HttpSession session){
        return userService.logout(session);
    }


    //탈퇴
    @DeleteMapping(path = "/withdraw")
    private ResponseEntity<CustomApiResponse<?>> withdrawMember(@RequestParam("userId") Long userId) {
        return userService.withdrawMember(userId);
    }


    //멘토(isMentor=true) 모두 조회
    @GetMapping("/mentors/all")
    private ResponseEntity<CustomApiResponse<?>> getAllMentors(){
        ResponseEntity<CustomApiResponse<?>> result =  userService.getAllMentors();
        return result;
    }

    // 모든 멘티 조회
    @GetMapping("/mentees/all")
    private ResponseEntity<CustomApiResponse<?>> getAllMentees() {
        return userService.getAllMentees();
    }


    //멘토 상세 조회
    @GetMapping()
    private ResponseEntity<CustomApiResponse<?>> getMentorDetail(
            @RequestParam("userId") Long userId){
        System.out.println("LOG userId " + userId);
        ResponseEntity<CustomApiResponse<?>> result = userService.getMentorDetail(userId);
        return result;
    }

    //멘토 수정페이지 접근 권한 검증
    @GetMapping("/validateEditAccess")
    public ResponseEntity<CustomApiResponse<?>> validateEditAccess(@RequestParam("userId") Long userId, HttpSession session) {
        Long loggedInUserId = (Long) session.getAttribute("userId");
        if (loggedInUserId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.UNAUTHORIZED.value(), "로그인 정보가 없습니다."));
        }
        return userService.validateEditAccess(loggedInUserId, userId);
    }
}
