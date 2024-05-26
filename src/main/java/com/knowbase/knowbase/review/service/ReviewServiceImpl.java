package com.knowbase.knowbase.review.service;

import com.knowbase.knowbase.comments.dto.WriteCommentdto;
import com.knowbase.knowbase.domain.Review;
import com.knowbase.knowbase.domain.User;
import com.knowbase.knowbase.review.dto.ReviewCreateDto;
import com.knowbase.knowbase.review.repository.ReviewRepository;
import com.knowbase.knowbase.users.repository.UserRepository;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    //후기 작성
    @Override
    public ResponseEntity<CustomApiResponse<?>> createReview(ReviewCreateDto.Req req) {
        //멘티 찾기
        Optional<User> findmentee = userRepository.findById(req.getMenteeId());
        //멘토 찾기
        Optional<User> findmentor = userRepository.findById(req.getMentorId());
        if (findmentor.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body(CustomApiResponse.createFailWithout(HttpStatus.FORBIDDEN.value(),
                                "해당 멘토는 존재하지 않습니다."));

        }

        //리뷰 엔티티
        Review createReview = Review.builder()
                .mentorId(findmentor.get())
                .menteeId(findmentee.get())
                .reviewTitle(req.getReviewTitle())
                .nickname(findmentee.get().getNickname())
                .beforeReImgPath(req.getBeforeReImgPath())
                .afterReImgPath(req.getAfterReImgPath())
                .reviewContent(req.getReviewContent())
                .satisfaction(req.getSatisfaction())
                .period(req.getPeriod())
                .budget(req.getBudget())
                .build();

        //연관관계 설정
        createReview.createReview(findmentor.get(), findmentee.get());

        //엔티티 저장
        Review savedReview = reviewRepository.save(createReview);

        //응답 dto 생성
        ReviewCreateDto.CreateReview responseDto = new ReviewCreateDto.CreateReview(savedReview.getReviewId(),savedReview.getCreateAt());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CustomApiResponse.createSuccess(
                        HttpStatus.OK.value(),
                        responseDto,
                        "후기가 작성 되었습니다."));

    }
}
