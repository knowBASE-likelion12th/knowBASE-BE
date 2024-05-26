package com.knowbase.knowbase.caterories.controller;

import com.knowbase.knowbase.caterories.dto.CreateCategoryDto;
import com.knowbase.knowbase.caterories.service.CategoryService;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/category")
public class CategoryController {
    private final CategoryService categoryService;

    //카테고리 생성
    @PostMapping("/create")
    private ResponseEntity<CustomApiResponse<?>> createCategory(
            @Valid @RequestBody CreateCategoryDto.Req categoryDto){
        ResponseEntity<CustomApiResponse<?>> category = categoryService.createCategory(categoryDto);
        return category;
    }
}
