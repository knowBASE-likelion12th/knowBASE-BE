package com.knowbase.knowbase.comments.repository;

import com.knowbase.knowbase.domain.Comment;
import com.knowbase.knowbase.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId);
}