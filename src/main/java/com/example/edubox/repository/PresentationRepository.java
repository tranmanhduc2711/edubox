package com.example.edubox.repository;

import com.example.edubox.constant.EPresentType;
import com.example.edubox.entity.Presentation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PresentationRepository extends JpaRepository<Presentation, Integer> {
    @Query("SELECT p FROM Presentation p WHERE p.status = 'A' AND" +
            " ( p.presentType = :presentType OR :presentType IS NULL ) AND " +
            " ( p.code = :code OR :code IS NULL) AND " +
            " ( p.group.groupCode = :groupCode OR :groupCode IS NULL)")
    List<Presentation> findPresentations(EPresentType presentType, String code,String groupCode);

    Optional<Presentation> findByCode(String code);

    Presentation findPresentationByCode(String code);
}
