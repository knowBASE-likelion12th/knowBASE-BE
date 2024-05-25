package com.knowbase.knowbase.introduces.repository;

import com.knowbase.knowbase.domain.Introduce;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IntroduceRepository extends JpaRepository<Introduce, Long> {
}
