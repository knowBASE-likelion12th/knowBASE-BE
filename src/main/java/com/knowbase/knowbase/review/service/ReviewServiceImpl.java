package com.knowbase.knowbase.review.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.knowbase.knowbase.comments.dto.CommentListDto;
import com.knowbase.knowbase.comments.dto.WriteCommentdto;

import com.knowbase.knowbase.domain.*;

import com.knowbase.knowbase.domain.Review;
import com.knowbase.knowbase.domain.User;

import com.knowbase.knowbase.review.dto.HighStarAvgDto;
import com.knowbase.knowbase.review.dto.ReviewCreateDto;
import com.knowbase.knowbase.review.dto.ReviewListDto;
import com.knowbase.knowbase.review.dto.ReviewUpdateDto;
import com.knowbase.knowbase.review.repository.ReviewRepository;
import com.knowbase.knowbase.users.repository.UserRepository;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import com.knowbase.knowbase.util.service.S3UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;


    private final S3UploadService s3UploadService;
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    //후기 작성
    @Override
    public ResponseEntity<CustomApiResponse<?>> createReview(ReviewCreateDto.Req req) {
        try{
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

            String imgBeforePath = null;
            if (req.getBeforeReImgPath() != null && !req.getBeforeReImgPath().isEmpty()) {
                imgBeforePath = s3UploadService.saveFile(req.getBeforeReImgPath());
            }
            String imgAfterPath = null;
            if (req.getAfterReImgPath() != null && !req.getAfterReImgPath().isEmpty()) {
                imgAfterPath = s3UploadService.saveFile(req.getAfterReImgPath());
            }

            //후기 엔티티
            Review createReview = Review.builder()
                    .mentorId(findmentor.get())
                    .menteeId(findmentee.get())
                    .reviewTitle(req.getReviewTitle())
                    .nickname(findmentee.get().getNickname())
                    .date(req.getDate())
                    .beforeReImgPath(imgBeforePath)
                    .afterReImgPath(imgAfterPath)
                    .reviewContent(req.getReviewContent())
                    .satisfaction(req.getSatisfaction())
                    .period(req.getPeriod())
                    .budget(req.getBudget())
                    .build();

            //연관관계 설정
            createReview.createReview(findmentor.get(), findmentee.get());

            //엔티티 저장
            reviewRepository.save(createReview);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(CustomApiResponse.createSuccess(
                            HttpStatus.OK.value(),
                            null,
                            "후기가 작성 되었습니다."));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 오류가 발생했습니다."));
        }
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
                    .status(HttpStatus.NOT_FOUND)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(),
                            "존재하지 않는 회원입니다."));
        }
        List<Review> findReview = reviewRepository.findByMenteeId(findUser.get());

        List<ReviewListDto.ReviewDto> reviewResponse = new ArrayList<>();



        for(Review review : findReview){
            //date yyyy.mm.dd 형식으로
            LocalDateTime date = review.getDate();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
            String formattedDate = date.format(formatter);

            reviewResponse.add(ReviewListDto.ReviewDto.builder()
                            .reviewId(review.getReviewId())
                            .mentorId(review.getMentorId().getUserId())
                            .menteeId(review.getMenteeId().getUserId())
                            .reviewTitle(review.getReviewTitle())
                            .nickname(review.getNickname())
                            .date(formattedDate)
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
        //해당 유저가 DB에 존재하는 유저인지
        Optional<User> findUser = userRepository.findById(mentorId);
        if(findUser.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(),
                            "존재하지 않는 회원입니다."));
        }
        List<Review> findReview = reviewRepository.findByMentorId(findUser.get());

        List<ReviewListDto.ReviewDto> reviewResponse = new ArrayList<>();

        for(Review review : findReview){
            //date yyyy.mm.dd 형식으로
            LocalDateTime date = review.getDate();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
            String formattedDate = date.format(formatter);

            reviewResponse.add(ReviewListDto.ReviewDto.builder()
                    .reviewId(review.getReviewId())
                    .mentorId(review.getMentorId().getUserId())
                    .menteeId(review.getMenteeId().getUserId())
                    .reviewTitle(review.getReviewTitle())
                    .nickname(review.getNickname())
                    .date(formattedDate)
                    .beforeReImgPath(review.getBeforeReImgPath())
                    .afterReImgPath(review.getAfterReImgPath())
                    .reviewContent(review.getReviewContent())
                    .satisfaction(review.getSatisfaction())
                    .period(review.getPeriod())
                    .budget(review.getBudget())
                    .build());
        }

        ReviewListDto.SearchReviewRes searchReviewRes = new ReviewListDto.SearchReviewRes(reviewResponse);
        CustomApiResponse<ReviewListDto.SearchReviewRes> res = CustomApiResponse.createSuccess(HttpStatus.OK.value(), searchReviewRes, "해당 멘토에게 달린 후기 조회 성공");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @Override
    public ResponseEntity<CustomApiResponse<?>> deleteReview(Long reviewId) {
        // 1. DB에 존재하는 후기인지 확인
        Optional<Review> findReview = reviewRepository.findById(reviewId);

        //존재하지 않는 후기라면
        if(findReview.isEmpty()){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(),
                            "해당 후기는 존재하지 않습니다."));
        }

        //2. 후기 삭제
        reviewRepository.delete(findReview.get());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CustomApiResponse.createSuccess(HttpStatus.OK.value(),null,"해당 후기가 삭제되었습니다"));
    }

    @Override
    public ResponseEntity<CustomApiResponse<?>> updateReview(ReviewUpdateDto.Req reviewUpdateDto) {
        try {

            // 2.1. 수정하려는 후기가 DB에 존재하는지 확인
            Optional<Review> findReview
                    = reviewRepository.findById(reviewUpdateDto.getReviewId());
            // 존재하지 않는다면 => 이미 삭제되었거나, 잘못된 요청
            if (findReview.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(CustomApiResponse.createFailWithout(
                                HttpStatus.NOT_FOUND.value(),
                                "수정하려는 후기가 존재하지 않습니다."));
            }

            Long reviewMemberId = findReview.get().getMenteeId().getUserId(); //게시물 작성자의 ID
            if (reviewMemberId != reviewUpdateDto.getMenteeId()) {
                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body(CustomApiResponse.createFailWithout(
                                HttpStatus.FORBIDDEN.value(),
                                "해당 유저는 수정 권한이 없습니다."));
            }

            //후기 수정
            //수정할 후기 가져옴
            Review review = findReview.get();

            //after만 수정
            if (reviewUpdateDto.getAfterReImgPath() != null && reviewUpdateDto.getBeforeReImgPath() == null) {
                String newAfterImg = s3UploadService.saveFile(reviewUpdateDto.getAfterReImgPath());
                if (review.getAfterReImgPath() != null) {
                    amazonS3.deleteObject(new DeleteObjectRequest(bucket, review.getAfterReImgPath()));
                }
                review.setAfterReImgPath(newAfterImg);
            }
            //before만 수정
            else if (reviewUpdateDto.getBeforeReImgPath() != null && reviewUpdateDto.getAfterReImgPath() == null) {
                String newBeforeImg = s3UploadService.saveFile(reviewUpdateDto.getBeforeReImgPath());
                if (review.getBeforeReImgPath() != null) {
                    amazonS3.deleteObject(new DeleteObjectRequest(bucket, review.getBeforeReImgPath()));
                }
                review.setAfterReImgPath(newBeforeImg);
            }
            //after 랑 before 둘 다 수정
            else if (reviewUpdateDto.getBeforeReImgPath() != null && reviewUpdateDto.getAfterReImgPath() != null) {
                String newAfterImg = s3UploadService.saveFile(reviewUpdateDto.getBeforeReImgPath());
                String newBeforeImg = s3UploadService.saveFile(reviewUpdateDto.getBeforeReImgPath());
                if (review.getBeforeReImgPath() != null && review.getAfterReImgPath() != null) {
                    amazonS3.deleteObject(new DeleteObjectRequest(bucket, review.getAfterReImgPath()));
                    amazonS3.deleteObject(new DeleteObjectRequest(bucket, review.getBeforeReImgPath()));

                }
                review.setAfterReImgPath(newAfterImg);
                review.setBeforeReImgPath(newBeforeImg);
            }

            review.changeReview(
                    reviewUpdateDto.getReviewTitle(),
                    review.getBeforeReImgPath(),
                    review.getAfterReImgPath(),
                    reviewUpdateDto.getReviewContent(),
                    reviewUpdateDto.getSatisfaction(),
                    reviewUpdateDto.getPeriod(),
                    reviewUpdateDto.getBudget());
            reviewRepository.flush(); //수정된 내용 DB에 즉시 적용


            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(CustomApiResponse.createSuccess(HttpStatus.OK.value(), null, "후기가 수정되었습니다"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 오류가 발생했습니다."));
        }
    }
}
