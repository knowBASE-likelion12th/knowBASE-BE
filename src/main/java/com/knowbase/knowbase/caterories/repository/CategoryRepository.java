package com.knowbase.knowbase.caterories.repository;

import com.knowbase.knowbase.domain.Category;
import com.knowbase.knowbase.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByUserName(User userId);
}
