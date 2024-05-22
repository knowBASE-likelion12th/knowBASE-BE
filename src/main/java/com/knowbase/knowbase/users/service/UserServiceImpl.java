package com.knowbase.knowbase.users.service;


import com.knowbase.knowbase.domain.User;
import com.knowbase.knowbase.users.dto.MenteeSignUpDto;
import com.knowbase.knowbase.users.dto.MentorSignUpDto;
import com.knowbase.knowbase.users.repository.UserRepository;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Builder
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    //멘토 회원가입
    @Override
    public ResponseEntity<CustomApiResponse<?>> mentoSignup(MentorSignUpDto memberSignUpDto) {
        String userName = memberSignUpDto.getUserName();
        Optional<User> findUser = userRepository.findByUserName(userName);

        //1. 이미 존재하는 아이디인지 검사 -> 회원가입 x
        if(findUser.isPresent()){
            CustomApiResponse<Object> failResponse = CustomApiResponse.createFailWithout(HttpStatus.BAD_REQUEST.value(), "이미 사용중인 아이디입니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(failResponse);
        }

        // 2. 존재하지 않는 아이디 -> 회원가입 O
        User newUser = User.builder()
                .userName(memberSignUpDto.getUserName())
                .password(memberSignUpDto.getPassword())
                .name(memberSignUpDto.getName())
                .isMentor(memberSignUpDto.getIsMentor())
                .employmentPath(memberSignUpDto.getEmploymentPath())
                .profImgPath(memberSignUpDto.getProfileImgPath())
                .mentoringPath(memberSignUpDto.getMentoringPath())
                .mentorContent(memberSignUpDto.getMentorContent())
                .build();
        //회원 저장
        userRepository.save(newUser);

        //응답
        return ResponseEntity.status(HttpStatus.OK.value()).body(CustomApiResponse.createSuccess(HttpStatus.OK.value(), null,"멘토 회원가입에 성공하였습니다."));
    }

    //멘티 회원가입
    @Override
    public ResponseEntity<CustomApiResponse<?>> menteeSignup(MenteeSignUpDto menteeSignUpDto) {
        String userName = menteeSignUpDto.getUserName();
        Optional<User> findUser = userRepository.findByUserName(userName);

        //1. 이미 존재하는 아이디인지 검사 -> 회원가입 x
        if(findUser.isPresent()){
            CustomApiResponse<Object> failResponse = CustomApiResponse.createFailWithout(HttpStatus.BAD_REQUEST.value(), "이미 사용중인 아이디입니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(failResponse);
        }

        // 2. 존재하지 않는 아이디 -> 회원가입 O
        User newUser = User.builder()
                .userName(menteeSignUpDto.getUserName())
                .password(menteeSignUpDto.getPassword())
                .name(menteeSignUpDto.getName())
                .isMentor(menteeSignUpDto.getIsMentor())
                .profImgPath(menteeSignUpDto.getProfileImgPath())
                .build();
        //회원 저장
        userRepository.save(newUser);

        //응답
        return ResponseEntity.status(HttpStatus.OK.value()).body(CustomApiResponse.createSuccess(HttpStatus.OK.value(), null,"멘티 회원가입에 성공하였습니다."));
    }
}
