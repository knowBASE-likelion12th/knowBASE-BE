package com.knowbase.knowbase.caterories.service;

import com.knowbase.knowbase.caterories.dto.CreateCategoryDto;
import com.knowbase.knowbase.caterories.repository.CategoryRepository;
import com.knowbase.knowbase.domain.Category;
import com.knowbase.knowbase.domain.User;
import com.knowbase.knowbase.users.repository.UserRepository;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    //카테고리 생성
    @Override
    public ResponseEntity<CustomApiResponse<?>> createCategory(CreateCategoryDto.Req categoryDto) {
        try {
            // 카테고리 작성자가 DB에 존재하는 지 확인
            Optional<User> findUser = userRepository.findById(categoryDto.getUserId());
            if (findUser.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(),
                                "해당 유저는 존재하지 않습니다."));
            }

            // 중복 카테고리 체크
            // Optional<Category> existingCategory = categoryRepository.findByName(categoryDto.getName());
            // if (existingCategory.isPresent()) {
            //     return ResponseEntity
            //             .status(HttpStatus.CONFLICT)
            //             .body(CustomApiResponse.createFailWithout(HttpStatus.CONFLICT.value(),
            //                     "이미 존재하는 카테고리입니다."));
            // }

            Category newCategory = categoryDto.toEntity();
            newCategory.createCategory(findUser.get()); // 연관관계 설정
            Category savedCategory = categoryRepository.save(newCategory);

            // 응답
            CustomApiResponse<CreateCategoryDto.CreateCategory> res = CustomApiResponse.createSuccess(HttpStatus.OK.value(), null, "카테고리가 작성되었습니다.");
            return ResponseEntity.ok(res);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "내부 서버 오류가 발생했습니다."));
        }
    }
}
