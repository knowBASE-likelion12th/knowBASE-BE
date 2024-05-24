package com.knowbase.knowbase.posts.repository;

import com.knowbase.knowbase.domain.Post;
import com.knowbase.knowbase.domain.User;
import com.knowbase.knowbase.util.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreateAtDesc();

    List<Post> findByUserId(User userId);
}
