package com.knowbase.knowbase.users.service;


import com.knowbase.knowbase.domain.User;
import com.knowbase.knowbase.users.dto.MenteeSignUpDto;
import com.knowbase.knowbase.users.dto.MentorListDto;
import com.knowbase.knowbase.users.dto.MentorSignUpDto;
import com.knowbase.knowbase.users.dto.UserSignInDto;
import com.knowbase.knowbase.users.repository.UserRepository;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import jakarta.servlet.http.HttpSession;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Builder
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final HttpSession session;

    //멘토 회원가입
    @Override
    public ResponseEntity<CustomApiResponse<?>> mentoSignup(MentorSignUpDto memtorSignUpDto) {
        String userName = memtorSignUpDto.getUserName();
        Optional<User> findUser = userRepository.findByUserName(userName);

        //1. 이미 존재하는 아이디인지 검사 -> 회원가입 x
        if (findUser.isPresent()) {
            CustomApiResponse<Object> failResponse = CustomApiResponse.createFailWithout(HttpStatus.BAD_REQUEST.value(), "이미 사용중인 아이디입니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(failResponse);
        }

        // 2. 존재하지 않는 아이디 -> 회원가입 O
        User newUser = User.builder()
                .userName(memtorSignUpDto.getUserName())
                .password(memtorSignUpDto.getPassword())
                .name(memtorSignUpDto.getName())
                .isMentor(memtorSignUpDto.getIsMentor())
                .employmentPath(memtorSignUpDto.getEmploymentPath())
                .gender(memtorSignUpDto.getGender())
                .age(memtorSignUpDto.getAge())
                .profImgPath(memtorSignUpDto.getProfileImgPath())
                .mentoringPath(memtorSignUpDto.getMentoringPath())
                .mentorContent(memtorSignUpDto.getMentorContent())
                .build();
        //회원 저장
        userRepository.save(newUser);

        //응답
        return ResponseEntity.status(HttpStatus.OK.value()).body(CustomApiResponse.createSuccess(HttpStatus.OK.value(), null, "멘토 회원가입에 성공하였습니다."));
    }

    //멘티 회원가입
    @Override
    public ResponseEntity<CustomApiResponse<?>> menteeSignup(MenteeSignUpDto menteeSignUpDto) {
        String userName = menteeSignUpDto.getUserName();
        Optional<User> findUser = userRepository.findByUserName(userName);

        //1. 이미 존재하는 아이디인지 검사 -> 회원가입 x
        if (findUser.isPresent()) {
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
        return ResponseEntity.status(HttpStatus.OK.value()).body(CustomApiResponse.createSuccess(HttpStatus.OK.value(), null, "멘티 회원가입에 성공하였습니다."));
    }

    //로그인 // 응답 : 멘토Id, isMentor, 비번
    @Override
    public ResponseEntity<CustomApiResponse<?>> signIn(UserSignInDto userSignInDto){
        //회원이 DB에 존재하는지 확인
        Optional<User> findUser = userRepository.findByUserName(userSignInDto.getUserName());

        //회원이 존재하지 않을 때
        if (findUser.isEmpty()) {
            CustomApiResponse<Void> failResponse = CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "존재하지 않는 회원입니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(failResponse);
        }
        // 비밀번호가 일치하지 않을 때
        if (!userSignInDto.getPassword().equals(findUser.get().getPassword())) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.UNAUTHORIZED.value(), "비밀번호가 일치하지 않습니다."));
        }


        //로그인 성공
        //session.setAttribute("userId", findUser.get().getUserId());
        User user = findUser.get();
        UserSignInDto.AccountEnter accountEnter =  new UserSignInDto.AccountEnter(
                user.getUserId(),
                user.getPassword(),
                user.getIsMentor()
        );
        return ResponseEntity.status(HttpStatus.OK).body(CustomApiResponse.createSuccess(HttpStatus.OK.value(), accountEnter, "로그인에 성공하였습니다."));
    }

    //회원(멘토) 전체 조회
    @Override
    public ResponseEntity<CustomApiResponse<?>> getAllMentors() {
        List<User> mentors = userRepository.findByIsMentorTrue();
        List<MentorListDto.MentorResponse> mentorResponses = mentors.stream().map(user -> MentorListDto.MentorResponse.builder()
                .id(user.getUserId())
                .userName(user.getUserName())
                .name(user.getName())
                .profileImgPath(user.getProfImgPath())
                .mentorContent(user.getMentorContent())
                .mentoringPath(user.getMentoringPath())
                .employmentPath(user.getEmploymentPath())
                .isMentor(user.getIsMentor())
                .build()).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK)
                .body(CustomApiResponse.createSuccess(HttpStatus.OK.value(), new MentorListDto.SearchMentorsRes(mentorResponses), "멘토 전체 조회에 성공하였습니다."));
    }

    //특정 회원 조회
    @Override
    public ResponseEntity<CustomApiResponse<?>> getMentorDetail(Long userId) {
        Optional<User> mentor = userRepository.findById(userId);
        if (mentor.isEmpty() || !mentor.get().getIsMentor()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "존재하지 않는 멘토입니다."));
        }

        User user = mentor.get();
        MentorListDto.MentorResponse mentorResponse = MentorListDto.MentorResponse.builder()
                .id(user.getUserId())
                .userName(user.getUserName())
                .name(user.getName())
                .profileImgPath(user.getProfImgPath())
                .mentorContent(user.getMentorContent())
                .mentoringPath(user.getMentoringPath())
                .employmentPath(user.getEmploymentPath())
                .isMentor(user.getIsMentor())
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .body(CustomApiResponse.createSuccess(HttpStatus.OK.value(), mentorResponse, "멘토 상세 조회에 성공하였습니다."));
    }

    //멘토 수정페이지 접근 권한 검증
    @Override
    public ResponseEntity<CustomApiResponse<?>> validateEditAccess(Long loggedInUserId, Long userId) {
        if (loggedInUserId.equals(userId)) {
            return ResponseEntity.ok(CustomApiResponse.createSuccess(HttpStatus.OK.value(),  null, "수정 페이지에 접속 가능합니다."));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(CustomApiResponse.createFailWithout(HttpStatus.FORBIDDEN.value(), "접근 권한이 없습니다."));
    }


    //회원탈퇴
    @Override
    public ResponseEntity<CustomApiResponse<?>> withdrawMember(Long userId) {
        //DB에 존재하는 회원인지 확인
        Optional<User> findUser = userRepository.findById(userId);
        if (findUser.isEmpty()) {
            CustomApiResponse<Void> failResponse = CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "존재하지 않는 회원입니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(failResponse);
        }
        //탈퇴
        userRepository.delete(findUser.get());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CustomApiResponse.createSuccess(HttpStatus.OK.value(),null,"회원이 정상적으로 탈퇴되었습니다"));
    }

    //로그아웃
    @Override
    public ResponseEntity<CustomApiResponse<?>> logout(HttpSession session) {
        //session.invalidate();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CustomApiResponse.createSuccess(HttpStatus.OK.value(),null,"로그아웃 되었습니다."));
    }

}

