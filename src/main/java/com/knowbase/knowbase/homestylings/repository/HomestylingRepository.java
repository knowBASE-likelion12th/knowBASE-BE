package com.knowbase.knowbase.homestylings.repository;

import com.knowbase.knowbase.domain.HomeStyling;
import com.knowbase.knowbase.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HomestylingRepository extends JpaRepository<HomeStyling, Long> {

    List<HomeStyling> findByUserId(User userId);
}
