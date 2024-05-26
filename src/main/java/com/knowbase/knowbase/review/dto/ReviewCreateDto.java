package com.knowbase.knowbase.review.dto;

import com.knowbase.knowbase.domain.Review;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

public class ReviewCreateDto {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Req{

        @NotNull(message = "후기를 작성할 멘토의 userId를 입력해주세요")
        private Long mentorId;

        @NotNull(message = "후기를 작성하는 멘티의 userId를 입력해주세요")
        private Long menteeId;

        @NotNull(message = "리뷰 제목을 입력해주세요")
        private String reviewTitle;

        @NotNull(message = "작성자의 닉네임 입력해주세요")
        private String nickname;

        @NotNull(message = "작성 날짜를 입력해주세요")
        private String date;

        @NotNull(message = "멘토링 전 사진의 경로를 넣어주세요")
        private String beforeReImgPath;

        @NotNull(message = "멘토링 후 사진의 경로를 넣어주세요")
        private String afterReImgPath;

        @NotNull(message = "후기 내용을 입력해주세요")
        private String reviewContent;

        @NotNull(message = "만족도를 넣어주세요")
        private Long satisfaction;

        @NotNull(message = "멘토링 기간을 입력해주세요")
        private String period;

        @NotNull(message = "예산을 입력해주세요")
        private String budget;

    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CreateReview{
        private Long reviewId;
        private LocalDateTime createAt;

        public CreateReview(Long reviewId, LocalDateTime createAt) {
            this.reviewId = reviewId;
            this.createAt = createAt;
        }
    }



}
