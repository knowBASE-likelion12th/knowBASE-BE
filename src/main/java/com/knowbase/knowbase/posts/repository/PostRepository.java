package com.knowbase.knowbase.posts.repository;

import com.knowbase.knowbase.domain.Post;
import com.knowbase.knowbase.util.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

}
