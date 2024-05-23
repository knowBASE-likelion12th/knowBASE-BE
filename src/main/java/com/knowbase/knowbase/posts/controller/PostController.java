package com.knowbase.knowbase.posts.controller;

import com.knowbase.knowbase.domain.Post;
import com.knowbase.knowbase.posts.dto.PostCreateDto;
import com.knowbase.knowbase.posts.dto.PostUpdateDto;
import com.knowbase.knowbase.posts.service.PostService;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import jakarta.persistence.PostUpdate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/post")
@RequiredArgsConstructor
public class PostController {
     private final PostService postService;

     //게시물 작성
     @PostMapping
    public ResponseEntity<CustomApiResponse<?>> createPost(
            @Valid @RequestBody PostCreateDto.Req postCreateDto){
         ResponseEntity<CustomApiResponse<?>> post = postService.createPost(postCreateDto);
         return post;
     }

    //게시물 수정
    @PutMapping
    public ResponseEntity<CustomApiResponse<?>> updatePost(
            @RequestParam("postId") Long postId,
            @RequestBody PostUpdateDto.Req postUpdateDto){
         ResponseEntity<CustomApiResponse<?>> result = postService.updatePost(postId, postUpdateDto);
         return result;
    }
    //게시물 삭제
    @DeleteMapping
    public ResponseEntity<CustomApiResponse<?>> deletePost(@RequestParam("postId") Long postId){
        ResponseEntity<CustomApiResponse<?>> result = postService.deletePost(postId);
         return result;
    }

    //특정 게시물 상세 조회
    @GetMapping
    public ResponseEntity<CustomApiResponse<?>> getPostDetail(@RequestParam("postId") Long postId){
        ResponseEntity<CustomApiResponse<?>> result = postService.getPostDatail(postId);
        return result;
    }

    //전체 게시글 조회
    @GetMapping("/all")
    public ResponseEntity<CustomApiResponse<?>> getAllPost(){
        ResponseEntity<CustomApiResponse<?>> result = postService.getAllPost();
        return result;
    }

    //최신순 게시글 조회
    @GetMapping("/recently")
    public ResponseEntity<CustomApiResponse<?>> getRecentPost(){
        ResponseEntity<CustomApiResponse<?>> result = postService.getRecentPost();
        return result;
    }

}
