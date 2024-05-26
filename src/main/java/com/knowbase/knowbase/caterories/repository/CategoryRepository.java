package com.knowbase.knowbase.caterories.repository;

import com.knowbase.knowbase.domain.Category;
import com.knowbase.knowbase.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByUserName(User userId);
    List<Category> findByInterestAndHousingTypeAndSpaceTypeAndStyle(String interest, String housingType, String spaceType, String style);
    List<Category> findByInterest(String interest);
    List<Category> findByHousingType(String housingType);
    List<Category> findBySpaceType(String spaceType);
    List<Category> findByStyle(String style);
    List<Category> findByInterestAndHousingType(String interest, String housingType);
    List<Category> findByInterestAndSpaceType(String interest, String spaceType);
    List<Category> findByInterestAndStyle(String interest, String style);
    List<Category> findByHousingTypeAndSpaceType(String housingType, String spaceType);
    List<Category> findByHousingTypeAndStyle(String housingType, String style);
    List<Category> findBySpaceTypeAndStyle(String spaceType, String style);
    List<Category> findByInterestAndHousingTypeAndSpaceType(String interest, String housingType, String spaceType);
    List<Category> findByInterestAndHousingTypeAndStyle(String interest, String housingType, String style);
    List<Category> findByHousingTypeAndSpaceTypeAndStyle(String housingType, String spaceType, String style);
    List<Category> findByInterestAndSpaceTypeAndStyle(String interest, String spaceType, String style);
}
