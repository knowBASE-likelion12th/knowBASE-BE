package com.knowbase.knowbase.comments.repository;

import com.knowbase.knowbase.domain.Comment;
import com.knowbase.knowbase.domain.Post;
import com.knowbase.knowbase.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);

    List<Comment> findByUser(User findUser);
}

