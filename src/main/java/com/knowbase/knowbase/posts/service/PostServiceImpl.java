package com.knowbase.knowbase.posts.service;

import com.knowbase.knowbase.domain.Post;
import com.knowbase.knowbase.domain.User;
import com.knowbase.knowbase.posts.dto.PostCreateDto;
import com.knowbase.knowbase.posts.repository.PostRepository;
import com.knowbase.knowbase.users.repository.UserRepository;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Builder
public class PostServiceImpl implements PostService{
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    //게시물 작성
    @Override
    public ResponseEntity<CustomApiResponse<?>> createPost(PostCreateDto.Req postCreateDto) {

        //댓글 작성자가 DB에 존재하는지 확인
        Optional<User> findUser = userRepository.findById(postCreateDto.getUserId());
        if(findUser.isEmpty()){
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.FORBIDDEN.value(),
                            "해당 유저는 존재하지 않습니다."));
        }
        Post newPost = postCreateDto.toEntity();
        newPost.createPost(findUser.get());
        Post savedPost = postRepository.save(newPost);

        PostCreateDto.CreatPost createdPostResponse = new PostCreateDto.CreatPost(savedPost.getPostId(),savedPost.getCreateAt());
        CustomApiResponse<PostCreateDto.CreatPost> res = CustomApiResponse.createSuccess(HttpStatus.OK.value(), createdPostResponse,"게시글이 작성되었습니다.");
        return ResponseEntity.ok(res);
    }
}


