package com.knowbase.knowbase.introduces.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IntroduceDto {
    private String introContent;
    private String availableTime;
    private String strength;
    private Long userId;
}
