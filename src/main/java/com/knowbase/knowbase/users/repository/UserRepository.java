package com.knowbase.knowbase.users.repository;

import com.knowbase.knowbase.domain.Post;
import com.knowbase.knowbase.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String userName);

    //모든 멘토 조회: 회원의 isMentor가 true(멘토)인 회원을 찾는 repository 메소드
    List<User> findByIsMentorTrue();
}
