package com.knowbase.knowbase.posts.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.knowbase.knowbase.domain.Post;
import com.knowbase.knowbase.domain.User;
import com.knowbase.knowbase.posts.dto.PostCreateDto;
import com.knowbase.knowbase.posts.dto.PostListDto;
import com.knowbase.knowbase.posts.dto.PostUpdateDto;
import com.knowbase.knowbase.posts.repository.PostRepository;
import com.knowbase.knowbase.users.repository.UserRepository;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import com.knowbase.knowbase.util.service.S3UploadService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    private final S3UploadService s3UploadService;
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    //게시물 작성
    @Override
    public ResponseEntity<CustomApiResponse<?>> createPost(PostCreateDto.Req postCreateDto) {
        try{
            System.out.println("postCreateDto.getUserId()"+postCreateDto.getUserId());
            //댓글 작성자가 DB에 존재하는지 확인
            Optional<User> findUser = userRepository.findById(postCreateDto.getUserId());
            if(findUser.isEmpty()){
                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body(CustomApiResponse.createFailWithout(HttpStatus.FORBIDDEN.value(),
                                "해당 유저는 존재하지 않습니다."));
            }

            String imgPath = null;
            System.out.println("postCreateDto.getPostImgPath()"+postCreateDto.getPostImgPath());
            if (postCreateDto.getPostImgPath() != null && !postCreateDto.getPostImgPath().isEmpty()) {
                imgPath = s3UploadService.saveFile(postCreateDto.getPostImgPath());
            }

            Post newPost = postCreateDto.toEntity(imgPath);
            newPost.createPost(findUser.get()); // 연관관계 설정
            postRepository.save(newPost);

            //응답 dto 불필요
            //PostCreateDto.CreatPost createdPostResponse = new PostCreateDto.CreatPost(savedPost.getPostId(),savedPost.getCreateAt());
            //응답

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(CustomApiResponse.createSuccess(HttpStatus.OK.value(),null,"게시글이 작성되었습니다"));
        }catch (DataAccessException dae) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "데이터베이스 오류가 발생했습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 오류가 발생했습니다."));
        }

    }

    //게시물 수정
    @Override
    public ResponseEntity<CustomApiResponse<?>> updatePost(Long postId, PostUpdateDto.Req postUpdateDto) {
        try{
            //1.수정하려는 게시물이 DB에 존재하는지 확인
            Optional<Post> findPost = postRepository.findById(postId);
            if(findPost.isEmpty()){
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(CustomApiResponse.createFailWithout(
                                HttpStatus.NOT_FOUND.value(),
                                "존재하지 않는 게시글 입니다."));
            }

            if(postUpdateDto == null){
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(CustomApiResponse.createFailWithout(
                                HttpStatus.BAD_REQUEST.value(),
                                "잘못된 요청 입니다."));
            }
            //게시물의 작성자와 수정하려는 유저가 일치하는지 확인
            Long postMemberId = findPost.get().getUserId().getUserId(); //게시물 작성자의 ID
            if(postMemberId != postUpdateDto.getUserId()){
                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body(CustomApiResponse.createFailWithout(
                                HttpStatus.FORBIDDEN.value(),
                                "해당 유저는 수정 권한이 없습니다."));
            }

            //게시물 수정
            //수정할 게시물 가져옴
            Post post = findPost.get();

            if(postUpdateDto.getPostImgPath() != null){
                String newImg = s3UploadService.saveFile(postUpdateDto.getPostImgPath());
                if(post.getPostImgPath() != null)
                {
                    amazonS3.deleteObject(new DeleteObjectRequest(bucket,post.getPostImgPath()));
                }
                post.setPostImgPath(newImg);
            }


            //제목 변경
            post.changePostTitle(postUpdateDto.getPostTitle());
            //내용 변경
            post.changePostContent(postUpdateDto.getPostContent());
            //이미지 변경
            post.changePostImgPath(post.getPostImgPath());

            postRepository.flush(); //변경 사항 db에 즉시 적용

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(CustomApiResponse.createSuccess(HttpStatus.OK.value(),null,"해당 게시글이 수정되었습니다"));
        }catch (DataAccessException dae) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "데이터베이스 오류가 발생했습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 오류가 발생했습니다."));
        }



    }

    //게시물 삭제
    @Override
    public ResponseEntity<CustomApiResponse<?>> deletePost(Long postId) {
        // 1. DB에 존재하는 게시물인지 확인
        Optional<Post> findPost = postRepository.findById(postId);

        //존재하지 않는 게시물이라면
        if(findPost.isEmpty()){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(),
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

        //updateAt yyyy.mm.dd 형식으로
        LocalDateTime updateAt = post.getUpdateAt();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        String formattedUpdateAt = updateAt.format(formatter);

        PostListDto.PostDto postResponse = new PostListDto.PostDto(
                post.getPostId(),
                post.getPostTitle(),
                post.getPostContent(),
                post.getPostImgPath(),
                user.getNickname(),
                post.getUserId().getUserId(),
                user.getProfImgPath(),
                formattedUpdateAt
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
                    .userId(post.getUserId().getUserId())
                    .postAuthorProfImg(post.getUserId().getProfImgPath())
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
                    .userId(post.getUserId().getUserId())
                    .postAuthorProfImg(post.getUserId().getProfImgPath())
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
                    .userId(post.getUserId().getUserId())
                    .postAuthorProfImg(post.getUserId().getProfImgPath())
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
                    .userId(post.getUserId().getUserId())
                    .postAuthorProfImg(post.getUserId().getProfImgPath())
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
                    .userId(post.getUserId().getUserId())
                    .postAuthorProfImg(post.getUserId().getProfImgPath())
                    .build());
        }

        PostListDto.SearchPostsRes searchPostsRes = new PostListDto.SearchPostsRes(postResponse);
        CustomApiResponse<PostListDto.SearchPostsRes> res = CustomApiResponse.createSuccess(HttpStatus.OK.value(), searchPostsRes, "댓글 적은 순 게시글 조회 성공");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}


