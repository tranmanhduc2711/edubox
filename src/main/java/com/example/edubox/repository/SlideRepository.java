package com.example.edubox.repository;

import com.example.edubox.constant.ECommonStatus;
import com.example.edubox.entity.Presentation;
import com.example.edubox.entity.Slide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SlideRepository extends JpaRepository<Slide,Integer> {
    @Query("SELECT s FROM Slide s WHERE s.status = :status AND" +
            " ( s.presentation = :presentation OR :presentation IS NULL ) AND " +
            " ( s.itemNo = :itemNo OR :itemNo IS NULL)")
    List<Slide> findSlides(Presentation presentation, Integer itemNo, ECommonStatus status);
    Optional<Slide> findSlideByPresentationAndAndItemNo(Presentation presentation,Integer itemNo);
}
