package com.knowbase.knowbase.review.dto;

import com.knowbase.knowbase.domain.Review;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

public class ReviewUpdateDto {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Req {
        @NotNull(message = "수정하려는 리뷰의 id를 입력해주세요")
        private Long reviewId;

        @NotNull(message = "리뷰 제목을 입력해주세요")
        private String reviewTitle;

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

        @NotNull(message = "수정하려는 멘티의 id를 넣어주세요")
        private Long menteeId;
    }

/*    @Getter
    @Builder
    @NoArgsConstructor
    public static class UpdateReview{
        private LocalDateTime updateAt;
        public UpdateReview(LocalDateTime updateAt) {
            this.updateAt = updateAt;
        }
    }*/
}
