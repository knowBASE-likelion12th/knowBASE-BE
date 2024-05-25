package com.knowbase.knowbase.roadmaps.repository;

import com.knowbase.knowbase.domain.Roadmap;
import com.knowbase.knowbase.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoadmapRepository extends JpaRepository<Roadmap, Long> {
    List<Roadmap> findByUserId(User userId);
}
