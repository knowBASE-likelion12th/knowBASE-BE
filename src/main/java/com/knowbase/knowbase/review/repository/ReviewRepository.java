package com.knowbase.knowbase.review.repository;

import com.knowbase.knowbase.domain.Review;
import com.knowbase.knowbase.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByMentorId(User mentor);

    List<Review> findByMenteeId(User user);
}
