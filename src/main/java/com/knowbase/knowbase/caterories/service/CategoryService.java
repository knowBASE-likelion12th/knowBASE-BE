package com.knowbase.knowbase.caterories.service;

import com.knowbase.knowbase.caterories.dto.CreateCategoryDto;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import org.springframework.http.ResponseEntity;

public interface CategoryService {
    ResponseEntity<CustomApiResponse<?>> createCategory(CreateCategoryDto.Req categoryDto);
}
