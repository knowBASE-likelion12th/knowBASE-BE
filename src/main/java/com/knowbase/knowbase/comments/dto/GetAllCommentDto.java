package com.knowbase.knowbase.comments.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GetAllCommentDto {
    private List<PostComment> comments = new ArrayList<>();
}
