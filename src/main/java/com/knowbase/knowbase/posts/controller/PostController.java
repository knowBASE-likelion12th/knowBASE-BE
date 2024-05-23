package com.knowbase.knowbase.posts.controller;

import com.knowbase.knowbase.posts.dto.PostCreateDto;
import com.knowbase.knowbase.posts.service.PostService;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/post")
@RequiredArgsConstructor
public class PostController {
     private final PostService postService;

     //게시물 작성
     @PostMapping
    public ResponseEntity<CustomApiResponse<?>> createPost(
            @Valid @RequestBody PostCreateDto.Req postCreateDto){
         ResponseEntity<CustomApiResponse<?>> createPost = postService.createPost(postCreateDto);
         return createPost;
     }

    //게시물 수정

    //게시물 삭제
}
