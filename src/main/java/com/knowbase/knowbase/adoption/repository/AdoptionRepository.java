package com.knowbase.knowbase.adoption.repository;

import com.knowbase.knowbase.domain.Adoption;
import com.knowbase.knowbase.domain.Comment;
import com.knowbase.knowbase.domain.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdoptionRepository extends JpaRepository<Adoption,Long> {
    Optional<Adoption> findByUserAndComment(User user, Comment comment);

    @Transactional //데이터베이스에서 업데이트나 삭제 작업을 실행할 땐 트랜잭션이 필요함
    @Modifying
    @Query("update Adoption a set a.isAdopt = true where a.id = :adoptionId")
    void serIsAdoptTrue(@Param("adoptionId") Long adoptionId);
}
