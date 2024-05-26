package com.knowbase.knowbase.adoption.controller;


import com.knowbase.knowbase.adoption.dto.AdoptionDto;
import com.knowbase.knowbase.adoption.repository.AdoptionRepository;
import com.knowbase.knowbase.adoption.service.AdoptionService;
import com.knowbase.knowbase.commentlike.dto.CommentLikeDto;
import com.knowbase.knowbase.commentlike.service.CommentLikeService;
import com.knowbase.knowbase.util.response.CustomApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/adoption")
@RequiredArgsConstructor
public class AdoptionController {
    private final AdoptionService adoptionService;

    @PostMapping("/adopt")
    public ResponseEntity<CustomApiResponse<?>> isAdopt(@Valid @RequestBody AdoptionDto.Req adoptionDto){
        return adoptionService.isAdopt(adoptionDto);
    }
}
