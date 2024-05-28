package com.knowbase.knowbase.users.service;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.knowbase.knowbase.caterories.repository.CategoryRepository;
import com.knowbase.knowbase.domain.Category;
import com.knowbase.knowbase.domain.User;
import com.knowbase.knowbase.users.dto.*;
import com.knowbase.knowbase.users.repository.UserRepository;
import com.knowbase.knowbase.util.exception.CustomValidationException;
import com.knowbase.knowbase.util.exception.EntityDuplicatedException;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import com.knowbase.knowbase.util.service.S3UploadService;
import com.knowbase.knowbase.util.valid.CustomValid;
import jakarta.servlet.http.HttpSession;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final HttpSession session;

    private final S3UploadService s3UploadService;
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    //멘토 회원가입
    @Override
    public ResponseEntity<CustomApiResponse<?>> mentoSignup(MentorSignUpDto mentorSignUpDto) {
        String userName = mentorSignUpDto.getUserName();
        Optional<User> findUser = userRepository.findByUserName(userName);

        //1. 이미 존재하는 아이디인지 검사 -> 회원가입 x
        if (findUser.isPresent()) {
            CustomApiResponse<Object> failResponse = CustomApiResponse.createFailWithout(HttpStatus.BAD_REQUEST.value(), "이미 존재하는 회원입니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(failResponse);
        }


        // 2. 존재하지 않는 아이디 -> 회원가입 O
        User newUser = User.builder()
                .userName(mentorSignUpDto.getUserName())
                .password(mentorSignUpDto.getPassword())
                .name(mentorSignUpDto.getName())
                .isMentor(mentorSignUpDto.getIsMentor())
                .nickname(mentorSignUpDto.getNickname())
                .employmentPath(mentorSignUpDto.getEmploymentPath())
                .gender(mentorSignUpDto.getGender())
                .age(mentorSignUpDto.getAge())
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
            CustomApiResponse<Object> failResponse = CustomApiResponse.createFailWithout(HttpStatus.BAD_REQUEST.value(), "이미 존재하는 회원입니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(failResponse);
        }

        // 2. 존재하지 않는 아이디 -> 회원가입 O
        User newUser = User.builder()
                .userName(menteeSignUpDto.getUserName())
                .password(menteeSignUpDto.getPassword())
                .name(menteeSignUpDto.getName())
                .nickname(menteeSignUpDto.getNickname())
                .isMentor(menteeSignUpDto.getIsMentor())
                .gender(menteeSignUpDto.getGender())
                .age(menteeSignUpDto.getAge())
                .build();
        //회원 저장
        userRepository.save(newUser);

        //응답
        return ResponseEntity.status(HttpStatus.OK.value()).body(CustomApiResponse.createSuccess(HttpStatus.OK.value(), null, "멘티 회원가입에 성공하였습니다."));
    }

    //로그인 // 응답 : 멘토Id, isMentor, 비번
    @Override
    public ResponseEntity<CustomApiResponse<?>> signIn(UserSignInDto userSignInDto) {
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
        UserSignInDto.AccountEnter accountEnter = new UserSignInDto.AccountEnter(
                user.getUserId(),
                user.getPassword(),
                user.getIsMentor()
        );
        return ResponseEntity.status(HttpStatus.OK).body(CustomApiResponse.createSuccess(HttpStatus.OK.value(), accountEnter, "로그인에 성공하였습니다."));
    }

    // 회원(멘토) 전체 조회
    @Override
    public ResponseEntity<CustomApiResponse<?>> getAllMentors() {
        try {
            List<User> mentors = userRepository.findByIsMentorTrue();

            if (mentors.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "등록된 멘토가 없습니다."));
            }

            List<MentorListDto.MentorResponse> mentorResponses = mentors.stream().map(user -> MentorListDto.MentorResponse.builder()
                    .userId(user.getUserId())
                    .userName(user.getUserName())
                    .nickName(user.getNickname())
                    .profileImgPath(user.getProfImgPath())
                    .mentorContent(user.getMentorContent())
                    .mentoringPath(user.getMentoringPath())
                    .isMentor(user.getIsMentor())
                    .build()).collect(Collectors.toList());

            return ResponseEntity.status(HttpStatus.OK)
                    .body(CustomApiResponse.createSuccess(HttpStatus.OK.value(), new MentorListDto.SearchMentorsRes(mentorResponses), "멘토 전체 조회에 성공하였습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "내부 서버 오류가 발생했습니다."));
        }
    }

    //멘토 최신순 조회
    @Override
    public ResponseEntity<CustomApiResponse<?>> getAllMentorsByCreateAt() {
        try {
            List<User> mentors = userRepository.findByIsMentorTrueOrderByCreateAtDesc();

            if (mentors == null || mentors.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "등록된 멘토가 없습니다."));
            }

            List<MentorListDto.MentorResponse> mentorResponses = mentors.stream()
                    .map(user -> MentorListDto.MentorResponse.builder()
                            .userId(user.getUserId())
                            .userName(user.getUserName())
                            .nickName(user.getNickname())
                            .profileImgPath(user.getProfImgPath())
                            .mentorContent(user.getMentorContent())
                            .mentoringPath(user.getMentoringPath())
                            .isMentor(user.getIsMentor())
                            .build())
                    .collect(Collectors.toList());

            return ResponseEntity.status(HttpStatus.OK)
                    .body(CustomApiResponse.createSuccess(HttpStatus.OK.value(), new MentorListDto.SearchMentorsRes(mentorResponses), "최신순 멘토 조회에 성공하였습니다."));

        } catch (DataAccessException dae) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "데이터베이스 오류가 발생했습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 오류가 발생했습니다."));
        }
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
                .userId(user.getUserId())
                .userName(user.getUserName())
                .nickName(user.getName())
                .profileImgPath(user.getProfImgPath())
                .isMentor(user.getIsMentor())
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .body(CustomApiResponse.createSuccess(HttpStatus.OK.value(), mentorResponse, "멘토 상세 조회에 성공하였습니다."));
    }

    //멘토 수정페이지 접근 권한 검증
    @Override
    public ResponseEntity<CustomApiResponse<?>> validateEditAccess(Long loggedInUserId, Long userId) {
        if (loggedInUserId.equals(userId)) {
            return ResponseEntity.ok(CustomApiResponse.createSuccess(HttpStatus.OK.value(), null, "수정 페이지에 접속 가능합니다."));
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
            CustomApiResponse<Void> failResponse = CustomApiResponse.createFailWithout(HttpStatus.UNAUTHORIZED.value(), "존재하지 않는 회원입니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(failResponse);
        }
        //탈퇴
        userRepository.delete(findUser.get());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CustomApiResponse.createSuccess(HttpStatus.OK.value(), null, "회원이 정상적으로 탈퇴되었습니다"));
    }

    //로그아웃
    @Override
    public ResponseEntity<CustomApiResponse<?>> logout(HttpSession session) {
        //session.invalidate();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CustomApiResponse.createSuccess(HttpStatus.OK.value(), null, "로그아웃 되었습니다."));
    }

    //모든 멘티 조회
    @Override
    public ResponseEntity<CustomApiResponse<?>> getAllMentees() {
        try {
            List<User> mentees = userRepository.findByIsMentorFalse();

            if (mentees == null || mentees.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "등록된 멘티가 없습니다."));
            }

            List<MenteeListDto.MenteeResponse> menteeResponses = mentees.stream().map(user -> MenteeListDto.MenteeResponse.builder()
                    .userId(user.getUserId())
                    .userName(user.getUserName())
                    .nickName(user.getNickname())
                    .profileImgPath(user.getProfImgPath())
                    .isMentor(user.getIsMentor())
                    .build()).collect(Collectors.toList());

            return ResponseEntity.status(HttpStatus.OK)
                    .body(CustomApiResponse.createSuccess(HttpStatus.OK.value(), new MenteeListDto.SearchMenteesRes(menteeResponses), "멘티 전체 조회에 성공하였습니다."));

        } catch (DataAccessException dae) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "데이터베이스 오류가 발생했습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 오류가 발생했습니다."));
        }
    }

    // 회원 정보 수정
    @Override
    public ResponseEntity<CustomApiResponse<?>> updateUser(Long userId, UserUpdateDto userUpdateDto) {
        try {
            Optional<User> findUser = userRepository.findById(userId);

            if (findUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "존재하지 않는 회원입니다."));
            }

            User user = findUser.get();

            // Handle profile image upload and deletion
            if (userUpdateDto.getProfileImg() != null) {
                String newProfileImgPath = s3UploadService.saveFile(userUpdateDto.getProfileImg());
                if (user.getProfImgPath() != null) {
                    amazonS3.deleteObject(new DeleteObjectRequest(bucket, user.getProfImgPath()));
                }
                user.setProfImgPath(newProfileImgPath);
            }

            // Handle mentoring image upload and deletion
            if (userUpdateDto.getMentoringImg() != null) {
                String newMentoringImgPath = s3UploadService.saveFile(userUpdateDto.getMentoringImg());
                if (user.getMentoringPath() != null) {
                    amazonS3.deleteObject(new DeleteObjectRequest(bucket, user.getMentoringPath()));
                }
                user.setMentoringPath(newMentoringImgPath);
            }

            user.updateProfile(userUpdateDto.getUserName(), userUpdateDto.getNickName(), user.getProfImgPath(), user.getMentoringPath(), userUpdateDto.getMentorContent());
            userRepository.save(user);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(CustomApiResponse.createSuccess(HttpStatus.OK.value(), null, "회원 정보가 성공적으로 수정되었습니다."));
        } catch (DataAccessException dae) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "데이터베이스 오류가 발생했습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 오류가 발생했습니다."));
        }
    }
    // 카테고리별 멘토 조회
    @Override
    public ResponseEntity<CustomApiResponse<?>> searchMentorsByCategory(String interest, String housingType, String spaceType, String style) {
        List<Category> categories;

        try {
            if (interest != null && housingType != null && spaceType != null && style != null) {
                categories = categoryRepository.findByInterestAndHousingTypeAndSpaceTypeAndStyle(interest, housingType, spaceType, style);
            } else if (interest != null && housingType != null && spaceType != null) {
                categories = categoryRepository.findByInterestAndHousingTypeAndSpaceType(interest, housingType, spaceType);
            } else if (interest != null && housingType != null && style != null) {
                categories = categoryRepository.findByInterestAndHousingTypeAndStyle(interest, housingType, style);
            } else if (interest != null && spaceType != null && style != null) {
                categories = categoryRepository.findByInterestAndSpaceTypeAndStyle(interest, spaceType, style);
            } else if (housingType != null && spaceType != null && style != null) {
                categories = categoryRepository.findByHousingTypeAndSpaceTypeAndStyle(housingType, spaceType, style);
            } else if (interest != null && housingType != null) {
                categories = categoryRepository.findByInterestAndHousingType(interest, housingType);
            } else if (interest != null && spaceType != null) {
                categories = categoryRepository.findByInterestAndSpaceType(interest, spaceType);
            } else if (interest != null && style != null) {
                categories = categoryRepository.findByInterestAndStyle(interest, style);
            } else if (housingType != null && spaceType != null) {
                categories = categoryRepository.findByHousingTypeAndSpaceType(housingType, spaceType);
            } else if (housingType != null && style != null) {
                categories = categoryRepository.findByHousingTypeAndStyle(housingType, style);
            } else if (spaceType != null && style != null) {
                categories = categoryRepository.findBySpaceTypeAndStyle(spaceType, style);
            } else if (interest != null) {
                categories = categoryRepository.findByInterest(interest);
            } else if (housingType != null) {
                categories = categoryRepository.findByHousingType(housingType);
            } else if (spaceType != null) {
                categories = categoryRepository.findBySpaceType(spaceType);
            } else if (style != null) {
                categories = categoryRepository.findByStyle(style);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(CustomApiResponse.createFailWithout(HttpStatus.BAD_REQUEST.value(), "검색 조건을 하나 이상 입력하세요."));
            }

            if (categories.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "해당 조건에 맞는 멘토가 없습니다."));
            }

            List<MentorListDto.MentorResponse> mentorResponses = categories.stream()
                    .map(Category::getUser)
                    .filter(User::getIsMentor)  // 멘토만 필터링
                    .map(user -> MentorListDto.MentorResponse.builder()
                            .userId(user.getUserId())
                            .userName(user.getUserName())
                            .nickName(user.getNickname())
                            .profileImgPath(user.getProfImgPath())
                            .mentorContent(user.getMentorContent())
                            .mentoringPath(user.getMentoringPath())
                            .isMentor(user.getIsMentor())
                            .build())
                    .collect(Collectors.toList());

            if (mentorResponses.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "해당 조건에 맞는 멘토가 없습니다."));
            }

            return ResponseEntity.status(HttpStatus.OK)
                    .body(CustomApiResponse.createSuccess(HttpStatus.OK.value(), new MentorListDto.SearchMentorsRes(mentorResponses), "멘토 검색에 성공하였습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "내부 서버 오류가 발생했습니다."));
        }
    }


    //만족도 높은 순 멘토 조회
    @Override
    public ResponseEntity<CustomApiResponse<?>> getMentorsBySatisfactionDesc() {
        try {
            List<User> mentors = userRepository.findMentorsBySatisfactionDesc();

            if (mentors == null || mentors.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "등록된 멘토가 없습니다."));
            }

            List<MentorListDto.MentorResponse> mentorResponses = mentors.stream()
                    .map(user -> MentorListDto.MentorResponse.builder()
                            .userId(user.getUserId())
                            .userName(user.getUserName())
                            .nickName(user.getNickname())
                            .profileImgPath(user.getProfImgPath())
                            .mentorContent(user.getMentorContent())
                            .mentoringPath(user.getMentoringPath())
                            .isMentor(user.getIsMentor())
                            .build())
                    .collect(Collectors.toList());

            return ResponseEntity.status(HttpStatus.OK)
                    .body(CustomApiResponse.createSuccess(HttpStatus.OK.value(), new MentorListDto.SearchMentorsRes(mentorResponses), "만족도 높은 순 멘토 조회에 성공하였습니다."));

        } catch (DataAccessException dae) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "데이터베이스 오류가 발생했습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 오류가 발생했습니다."));
        }
    }


    //만족도 낮은 순 멘토 조회
    @Override
    public ResponseEntity<CustomApiResponse<?>> getMentorsBySatisfactionAsc() {
        try {
            List<User> mentors = userRepository.findMentorsBySatisfactionAsc();

            if (mentors == null || mentors.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "등록된 멘토가 없습니다."));
            }

            List<MentorListDto.MentorResponse> mentorResponses = mentors.stream()
                    .map(user -> MentorListDto.MentorResponse.builder()
                            .userId(user.getUserId())
                            .userName(user.getUserName())
                            .nickName(user.getNickname())
                            .profileImgPath(user.getProfImgPath())
                            .mentorContent(user.getMentorContent())
                            .mentoringPath(user.getMentoringPath())
                            .isMentor(user.getIsMentor())
                            .build())
                    .collect(Collectors.toList());

            return ResponseEntity.status(HttpStatus.OK)
                    .body(CustomApiResponse.createSuccess(HttpStatus.OK.value(), new MentorListDto.SearchMentorsRes(mentorResponses), "만족도 낮은 순 멘토 조회에 성공하였습니다."));

        } catch (DataAccessException dae) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "데이터베이스 오류가 발생했습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 오류가 발생했습니다."));
        }
    }

    //아이디 중복확인
    @Override
    public ResponseEntity<CustomApiResponse<?>> checkUserIdExists(String userName) {

        // 사용자 아이디 중복 검증
        boolean userIdExists = userRepository.findByUserName(userName).isPresent();
        if (userIdExists) {
            CustomApiResponse<Object> failResponse = CustomApiResponse.createFailWithout(HttpStatus.UNAUTHORIZED.value(), "이미 사용중인 아이디입니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(failResponse);
        }

        // 응답
        CustomApiResponse<Object> resultBody = CustomApiResponse.createSuccess(HttpStatus.OK.value(), null, "사용 가능한 아이디입니다.");
        return ResponseEntity.status(HttpStatus.OK).body(resultBody);
    }
}

