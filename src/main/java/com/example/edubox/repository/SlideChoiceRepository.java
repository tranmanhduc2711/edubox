package com.example.edubox.repository;

import com.example.edubox.constant.ECommonStatus;
import com.example.edubox.entity.Slide;
import com.example.edubox.entity.SlideChoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SlideChoiceRepository extends JpaRepository<SlideChoice,Integer> {
    @Query("SELECT sc FROM SlideChoice sc WHERE sc.status = :status AND" +
            " ( sc.slide = :slide OR :slide IS NULL)")
    List<SlideChoice> findSlideChoices(Slide slide, ECommonStatus status);
}
