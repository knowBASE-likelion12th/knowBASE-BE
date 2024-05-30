package com.knowbase.knowbase.users.service;

import com.knowbase.knowbase.users.dto.MenteeSignUpDto;
import com.knowbase.knowbase.users.dto.MentorSignUpDto;
import com.knowbase.knowbase.users.dto.UserSignInDto;
import com.knowbase.knowbase.users.dto.UserUpdateDto;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;


public interface UserService {
    ResponseEntity<CustomApiResponse<?>> mentoSignup(MentorSignUpDto mentorSignUpDto);

    ResponseEntity<CustomApiResponse<?>> menteeSignup(MenteeSignUpDto menteeSignUpDto);

    ResponseEntity<CustomApiResponse<?>> signIn(UserSignInDto userSignInDto);

    ResponseEntity<CustomApiResponse<?>> getAllMentors();

    ResponseEntity<CustomApiResponse<?>> getAllMentorsByCreateAt();

    ResponseEntity<CustomApiResponse<?>> getMentorDetail(Long userId);

    ResponseEntity<CustomApiResponse<?>> validateEditAccess(Long loggedInUserId, Long userId);

    ResponseEntity<CustomApiResponse<?>> withdrawMember(Long userId);

    ResponseEntity<CustomApiResponse<?>> logout(HttpSession session);

    // 모든 멘티 조회
    ResponseEntity<CustomApiResponse<?>> getAllMentees();

    // 회원 정보 수정
    ResponseEntity<CustomApiResponse<?>> updateUser(Long userId, UserUpdateDto userUpdateDto, MultipartFile profileImg, MultipartFile profileImgPath);

    //카테고리 별 회원 조회
    ResponseEntity<CustomApiResponse<?>> searchMentorsByCategory(String interest, String housingType, String spaceType, String style);

    //리뷰 별점 순 조회
    ResponseEntity<CustomApiResponse<?>> getMentorsBySatisfactionDesc();
    ResponseEntity<CustomApiResponse<?>> getMentorsBySatisfactionAsc();

    ResponseEntity<CustomApiResponse<?>> checkUserIdExists(String userId);

    ResponseEntity<CustomApiResponse<?>> checkNicknameExists(String userName);
}
