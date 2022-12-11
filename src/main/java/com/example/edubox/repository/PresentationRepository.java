package com.example.edubox.repository;

import com.example.edubox.constant.EPresentType;
import com.example.edubox.entity.Presentation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PresentationRepository extends JpaRepository<Presentation,Integer> {
    @Query("SELECT p FROM Presentation p WHERE p.status = 'A' AND ( p.presentType = :presentType OR :presentType IS NULL )")
    List<Presentation> findPresentationByPresentType(EPresentType presentType);

    Optional<Presentation> findByCode(String code);
}
