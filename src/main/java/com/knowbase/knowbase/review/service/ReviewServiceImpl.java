package com.knowbase.knowbase.review.service;

import com.knowbase.knowbase.comments.dto.CommentListDto;
import com.knowbase.knowbase.comments.dto.WriteCommentdto;
import com.knowbase.knowbase.domain.Comment;
import com.knowbase.knowbase.domain.CommentLike;
import com.knowbase.knowbase.domain.Review;
import com.knowbase.knowbase.domain.User;
import com.knowbase.knowbase.review.dto.HighStarAvgDto;
import com.knowbase.knowbase.review.dto.ReviewCreateDto;
import com.knowbase.knowbase.review.dto.ReviewListDto;
import com.knowbase.knowbase.review.repository.ReviewRepository;
import com.knowbase.knowbase.users.repository.UserRepository;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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

    // 멘토의 평점
    @Override
    public ResponseEntity<CustomApiResponse<?>> getHighStarAvg(Long mentorId) {
        // 멘토Id로 User 객체 찾기
        Optional<User> mentor = userRepository.findById(mentorId);
        // 멘토Id로 후기 찾기
        List<Review> findHighStar = reviewRepository.findByMentorId(mentor.get());


        // 평점 합산 변수
        double totalStars = 0.0;

        // 평점 합산
        for (Review review : findHighStar) {
            totalStars += review.getSatisfaction(); // 각 후기의 평점을 가져와서 합산
        }

        // 평점 평균 계산
        double averageStar = totalStars / findHighStar.size();

        HighStarAvgDto res = new HighStarAvgDto(averageStar);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CustomApiResponse.createSuccess(
                        HttpStatus.OK.value(),
                        res, "해당 멘토의 평점 조회 성공"));
    }


    //내가 쓴 후기(멘티)
    @Override
    public ResponseEntity<CustomApiResponse<?>> getWroteReview(Long menteeId) {
        //해당 유저가 DB에 존재하는 유저인지
        Optional<User> findUser = userRepository.findById(menteeId);
        if(findUser.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.FORBIDDEN.value(),
                            "존재하지 않는 회원입니다."));
        }
        List<Review> findReview = reviewRepository.findByMenteeId(findUser.get());

        List<ReviewListDto.ReviewDto> reviewResponse = new ArrayList<>();

        for(Review review : findReview){
            reviewResponse.add(ReviewListDto.ReviewDto.builder()
                            .mentorId(review.getMentorId().getUserId())
                            .menteeId(review.getMenteeId().getUserId())
                            .reviewTitle(review.getReviewTitle())
                            .date(review.getDate())
                            .beforeReImgPath(review.getBeforeReImgPath())
                            .afterReImgPath(review.getAfterReImgPath())
                            .reviewContent(review.getReviewContent())
                            .satisfaction(review.getSatisfaction())
                            .period(review.getPeriod())
                            .budget(review.getBudget())
                            .build());
        }

        ReviewListDto.SearchReviewRes searchReviewRes = new ReviewListDto.SearchReviewRes(reviewResponse);
        CustomApiResponse<ReviewListDto.SearchReviewRes> res = CustomApiResponse.createSuccess(HttpStatus.OK.value(), searchReviewRes, "해당 멘티가 쓴 후기 조회 성공");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    //나의 후기(멘토)
    @Override
    public ResponseEntity<CustomApiResponse<?>> getMyReview(Long mentorId) {
        return null;
    }
}
