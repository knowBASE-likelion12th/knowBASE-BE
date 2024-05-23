package com.knowbase.knowbase.posts.service;

import com.knowbase.knowbase.posts.dto.PostCreateDto;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import org.springframework.http.ResponseEntity;

public interface PostService {
    //게시물 작성
    ResponseEntity<CustomApiResponse<?>> createPost(PostCreateDto.Req postCreateDto);
}