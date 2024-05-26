package com.knowbase.knowbase.users.repository;

import com.knowbase.knowbase.domain.Post;
import com.knowbase.knowbase.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String userName);

    //모든 멘토 조회: 회원의 isMentor가 true(멘토)인 회원을 찾는 repository 메소드
    List<User> findByIsMentorTrue();

    // 모든 멘티 조회
    List<User> findByIsMentorFalse();

    // 최신순 멘토 조회
    List<User> findByIsMentorTrueOrderByCreateAtDesc();

    // 멘토를 리뷰의 만족도 높은 순서로 정렬하여 조회
    @Query("SELECT r.mentorId FROM Review r WHERE r.mentorId.isMentor = true GROUP BY r.mentorId ORDER BY AVG(r.satisfaction) DESC")
    List<User> findMentorsBySatisfactionDesc();

    // 멘토를 리뷰의 만족도 낮은 순서로 정렬하여 조회
    @Query("SELECT r.mentorId FROM Review r WHERE r.mentorId.isMentor = true GROUP BY r.mentorId ORDER BY AVG(r.satisfaction) ASC")
    List<User> findMentorsBySatisfactionAsc();
}

