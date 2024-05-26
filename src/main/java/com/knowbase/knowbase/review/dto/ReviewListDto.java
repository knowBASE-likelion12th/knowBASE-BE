package com.knowbase.knowbase.review.dto;

import com.knowbase.knowbase.comments.dto.CommentListDto;
import lombok.*;

import java.util.List;

public class ReviewListDto {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewDto{
        private Long mentorId;
        private Long menteeId;
        private String reviewTitle;
        private String nickname;
        private String date;
        private String beforeReImgPath;
        private String afterReImgPath;
        private String reviewContent;
        private Long satisfaction;
        private String period;
        private String budget;
    }
    // 리뷰 조회
    @Getter @Setter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SearchReviewRes {
        private List<ReviewListDto.ReviewDto> review;
        public SearchReviewRes(List<ReviewListDto.ReviewDto> review) {
            this.review = review;
        }
    }
}
