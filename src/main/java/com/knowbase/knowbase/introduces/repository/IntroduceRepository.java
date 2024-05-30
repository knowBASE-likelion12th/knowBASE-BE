package com.knowbase.knowbase.introduces.repository;
import com.knowbase.knowbase.domain.Introduce;
import com.knowbase.knowbase.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IntroduceRepository extends JpaRepository<Introduce, Long> {
    List<Introduce> findByUserId(User userId);
}

