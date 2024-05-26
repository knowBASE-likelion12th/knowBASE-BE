package com.knowbase.knowbase.adoption.service;

import com.knowbase.knowbase.adoption.dto.AdoptionDto;
import com.knowbase.knowbase.adoption.repository.AdoptionRepository;
import com.knowbase.knowbase.comments.repository.CommentRepository;
import com.knowbase.knowbase.domain.Adoption;
import com.knowbase.knowbase.domain.Comment;
import com.knowbase.knowbase.domain.CommentLike;
import com.knowbase.knowbase.domain.User;
import com.knowbase.knowbase.users.repository.UserRepository;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdoptionServiceImpl implements AdoptionService {
    private final AdoptionRepository adoptionRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Transactional
    @Override
    public ResponseEntity<CustomApiResponse<?>> isAdopt(AdoptionDto.Req adoptionDto) {
        Optional<User> findUser = userRepository.findById(adoptionDto.getUserId());

        // 사용자 존재 여부 확인
        if (findUser.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "사용자를 찾을 수 없습니다."));
        }

        // 해당 댓글이 DB에 존재하는지 확인
        Optional<Comment> findComment = commentRepository.findById(adoptionDto.getCommentId());
        if (findComment.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.FORBIDDEN.value(), "삭제되었거나 존재하지 않는 댓글입니다."));
        }

        // 이미 채택이 되어 있는지 확인
        Optional<Adoption> findAdoption = adoptionRepository.findByUserAndComment(findUser.get(), findComment.get());
        if (findAdoption.isEmpty()) {
            Adoption adoption = Adoption.builder()
                    .comment(findComment.get())
                    .user(findUser.get())
                    .build();

            adoptionRepository.save(adoption);
            // 채택 상태를 업데이트
            adoptionRepository.serIsAdoptTrue(adoption.getId());

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(CustomApiResponse.createSuccess(HttpStatus.OK.value(), null, "채택이 되었습니다."));
        } else {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.BAD_REQUEST.value(), "이미 채택한 상태입니다."));
        }
    }

}
