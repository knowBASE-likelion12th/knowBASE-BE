package com.knowbase.knowbase.roadmaps.repository;

import com.knowbase.knowbase.domain.Roadmap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoadmapRepository extends JpaRepository<Roadmap, Long> {
}
