package com.knowbase.knowbase.posts.service;

import com.knowbase.knowbase.domain.Post;
import com.knowbase.knowbase.posts.dto.PostCreateDto;
import com.knowbase.knowbase.posts.dto.PostUpdateDto;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    //게시물 작성
    ResponseEntity<CustomApiResponse<?>> createPost(PostCreateDto.Req postCreateDto, MultipartFile postImgPath);

    ResponseEntity<CustomApiResponse<?>> updatePost(Long postId, PostUpdateDto.Req postUpdateDto);

    ResponseEntity<CustomApiResponse<?>> deletePost(Long postId);

    ResponseEntity<CustomApiResponse<?>> getPostDatail(Long postId);

    ResponseEntity<CustomApiResponse<?>> getAllPost();

    ResponseEntity<CustomApiResponse<?>> getRecentPost();

    ResponseEntity<CustomApiResponse<?>> getMyPost(Long userId);

    ResponseEntity<CustomApiResponse<?>> getDescCommentPost();

    ResponseEntity<CustomApiResponse<?>> getAsceCommentPost();
}
