package com.knowbase.knowbase.categories.service;

import com.knowbase.knowbase.categories.dto.CreateCategoryDto;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import org.springframework.http.ResponseEntity;

public interface CategoryService {
    ResponseEntity<CustomApiResponse<?>> createCategory(CreateCategoryDto.Req categoryDto);
}
