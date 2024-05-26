package com.knowbase.knowbase.posts.service;

import com.knowbase.knowbase.domain.Post;
import com.knowbase.knowbase.domain.User;
import com.knowbase.knowbase.posts.dto.PostCreateDto;
import com.knowbase.knowbase.posts.dto.PostListDto;
import com.knowbase.knowbase.posts.dto.PostUpdateDto;
import com.knowbase.knowbase.posts.repository.PostRepository;
import com.knowbase.knowbase.users.repository.UserRepository;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
        newPost.createPost(findUser.get()); //연관관계 설정
        Post savedPost = postRepository.save(newPost);

        //응답 dto
        PostCreateDto.CreatPost createdPostResponse = new PostCreateDto.CreatPost(savedPost.getPostId(),savedPost.getCreateAt());
        //응답
        CustomApiResponse<PostCreateDto.CreatPost> res = CustomApiResponse.createSuccess(HttpStatus.OK.value(), createdPostResponse,"게시글이 작성되었습니다.");
        return ResponseEntity.ok(res);
    }

    //게시물 수정
    @Override
    public ResponseEntity<CustomApiResponse<?>> updatePost(Long postId, PostUpdateDto.Req postUpdateDto) {
        //1.수정하려는 게시물이 DB에 존재하는지 확인
        Optional<Post> findPost = postRepository.findById(postId);
        if(findPost.isEmpty()){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(CustomApiResponse.createFailWithout(
                            HttpStatus.BAD_REQUEST.value(),
                            "수정하려는 게시물이 존재하지 않거나, 잘못된 요청입니다."));
        }
        //게시물의 작성자와 수정하려는 유저가 일치하는지 확인
        Long postMemberId = findPost.get().getUserId().getUserId(); //게시물 작성자의 ID
        if(postMemberId != postUpdateDto.getUserId() ){
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(CustomApiResponse.createFailWithout(
                            HttpStatus.FORBIDDEN.value(),
                            "해당 유저는 수정 권한이 없습니다."));
        }

        //게시물 수정
        Post post = findPost.get();
        post.changePostTitle(postUpdateDto.getPostTitle());
        post.changePostContent(postUpdateDto.getPostContent());
        post.changePostImgPath(postUpdateDto.getPostImgPath());

        //응답 dto 생성
        PostUpdateDto.UpdatePost updatePostResponse = PostUpdateDto.UpdatePost.builder()
                .updateAt(post.getUpdateAt())
                .build();
        //응답
        CustomApiResponse<PostUpdateDto.UpdatePost> res = CustomApiResponse.createSuccess(HttpStatus.OK.value(), updatePostResponse, "게시글이 수정되었습니다.");
        return ResponseEntity.ok(res);
    }

    //게시물 삭제
    @Override
    public ResponseEntity<CustomApiResponse<?>> deletePost(Long postId) {
        // 1. DB에 존재하는 게시물인지 확인
        Optional<Post> findPost = postRepository.findById(postId);

        //존재하지 않는 게시물이라면
        if(findPost.isEmpty()){
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.FORBIDDEN.value(),
                            "해당 게시글은 존재하지 않습니다."));
        }

        //2. 게시물 삭제
        postRepository.delete(findPost.get());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CustomApiResponse.createSuccess(HttpStatus.OK.value(),null,"해당 게시글이 삭제되었습니다"));
    }

    //특정 게시글 조회
    @Override
    public ResponseEntity<CustomApiResponse<?>> getPostDatail(Long postId) {

        //DB에 존재하는 게시글인지
        Optional<Post> findPost = postRepository.findById(postId);
        if(findPost.isEmpty()){
            CustomApiResponse<Void> res = CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "해당하는 게시글을 찾을 수 없습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }

        //user 정보
        Optional<User> findUser = userRepository.findById(findPost.get().getUserId().getUserId());


        Post post = findPost.get();
        User user = findUser.get();
        PostListDto.PostDto postResponse = new PostListDto.PostDto(
                post.getPostId(),
                post.getPostTitle(),
                post.getPostContent(),
                post.getPostImgPath(),
                user.getNickname(),
                post.getUserId().getUserId(),
                user.getProfImgPath(),
                post.getUpdateAt()
                );


        CustomApiResponse<PostListDto.PostDto> res = CustomApiResponse.createSuccess(HttpStatus.OK.value(), postResponse, "게시글 조회 성공");
        return ResponseEntity.ok(res);
    }

    //전체 게시글 조회
    @Override
    public ResponseEntity<CustomApiResponse<?>> getAllPost() {
        List<Post> posts = postRepository.findAll();

        List<PostListDto.PostDto> postResponse = new ArrayList<>();
        for(Post post : posts){
            postResponse.add(PostListDto.PostDto.builder()
                    .postId(post.getPostId())
                    .postTitle(post.getPostTitle())
                    .postContent(post.getPostContent())
                    .postImgPath(post.getPostImgPath())
                    .nickname(post.getUserId().getNickname())
                    .postAuthorProfImg(post.getUserId().getProfImgPath())
                    .updatedAt(post.getUpdateAt())
                    .build());
        }

        PostListDto.SearchPostsRes searchPostsRes = new PostListDto.SearchPostsRes(postResponse);
        CustomApiResponse<PostListDto.SearchPostsRes> res = CustomApiResponse.createSuccess(HttpStatus.OK.value(), searchPostsRes, "전체 게시글 조회 성공");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }


    //최신순 게시글 조회
    @Override
    public ResponseEntity<CustomApiResponse<?>> getRecentPost() {
        List<Post> findRecentPost = postRepository.findAllByOrderByCreateAtDesc();
        List<PostListDto.PostDto> postResponse = new ArrayList<>();

        for(Post post : findRecentPost){
            postResponse.add(PostListDto.PostDto.builder()
                    .postId(post.getPostId())
                    .postTitle(post.getPostTitle())
                    .postContent(post.getPostContent())
                    .postImgPath(post.getPostImgPath())
                    .nickname(post.getUserId().getNickname())
                    .postAuthorProfImg(post.getUserId().getProfImgPath())
                    .updatedAt(post.getUpdateAt())
                    .build());
        }

        PostListDto.SearchPostsRes searchPostsRes = new PostListDto.SearchPostsRes(postResponse);
        CustomApiResponse<PostListDto.SearchPostsRes> res = CustomApiResponse.createSuccess(HttpStatus.OK.value(), searchPostsRes, "최신순 게시글 조회 성공");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    //내가 쓴 게시물 조회
    @Override
    public ResponseEntity<CustomApiResponse<?>> getMyPost(Long userId) {

        Optional<User> OptionalUser = userRepository.findById(userId);

        //해당 userId를 가진 유저가 쓴 게시물 찾기
        List<Post> findPost = postRepository.findByUserId(OptionalUser.get());


        List<PostListDto.PostDto> postResponse = new ArrayList<>();
        for(Post post : findPost){
            postResponse.add(PostListDto.PostDto.builder()
                    .postId(post.getPostId())
                    .postTitle(post.getPostTitle())
                    .postContent(post.getPostContent())
                    .postImgPath(post.getPostImgPath())
                    .nickname(post.getUserId().getNickname())
                    .postAuthorProfImg(post.getUserId().getProfImgPath())
                    .updatedAt(post.getUpdateAt())
                    .build());
        }

        PostListDto.SearchPostsRes searchPostsRes = new PostListDto.SearchPostsRes(postResponse);
        CustomApiResponse<PostListDto.SearchPostsRes> res = CustomApiResponse.createSuccess(HttpStatus.OK.value(), searchPostsRes, "내가 쓴 게시글 조회 성공");
        return ResponseEntity.status(HttpStatus.OK).body(res);

    }

    //댓글 많은 순 게시물 조회
    @Override
    public ResponseEntity<CustomApiResponse<?>> getDescCommentPost() {

        //댓글 많은 순으로
        List<Post> posts = postRepository.findAllByCommentsCountDesc();

        List<PostListDto.PostDto> postResponse = new ArrayList<>();
        for(Post post : posts){
            postResponse.add(PostListDto.PostDto.builder()
                    .postId(post.getPostId())
                    .postTitle(post.getPostTitle())
                    .postContent(post.getPostContent())
                    .postImgPath(post.getPostImgPath())
                    .nickname(post.getUserId().getNickname())
                    .postAuthorProfImg(post.getUserId().getProfImgPath())
                    .updatedAt(post.getUpdateAt())
                    .build());
        }

        PostListDto.SearchPostsRes searchPostsRes = new PostListDto.SearchPostsRes(postResponse);
        CustomApiResponse<PostListDto.SearchPostsRes> res = CustomApiResponse.createSuccess(HttpStatus.OK.value(), searchPostsRes, "댓글 많은 순 게시글 조회 성공");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }


    //댓글 적은 순 게시물 조회
    @Override
    public ResponseEntity<CustomApiResponse<?>> getAsceCommentPost() {
        //댓글 적은 순으로
        List<Post> posts = postRepository.findAllByCommentsCountAsc();


        List<PostListDto.PostDto> postResponse = new ArrayList<>();
        for(Post post : posts){
            postResponse.add(PostListDto.PostDto.builder()
                    .postId(post.getPostId())
                    .postTitle(post.getPostTitle())
                    .postContent(post.getPostContent())
                    .postImgPath(post.getPostImgPath())
                    .nickname(post.getUserId().getNickname())
                    .postAuthorProfImg(post.getUserId().getProfImgPath())
                    .updatedAt(post.getUpdateAt())
                    .build());
        }

        PostListDto.SearchPostsRes searchPostsRes = new PostListDto.SearchPostsRes(postResponse);
        CustomApiResponse<PostListDto.SearchPostsRes> res = CustomApiResponse.createSuccess(HttpStatus.OK.value(), searchPostsRes, "댓글 적은 순 게시글 조회 성공");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}


