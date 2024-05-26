package com.knowbase.knowbase.adoption.service;

import com.knowbase.knowbase.adoption.dto.AdoptionDto;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import org.springframework.http.ResponseEntity;

public interface AdoptionService {
    ResponseEntity<CustomApiResponse<?>> isAdopt(AdoptionDto.Req adoptionDto);
}
