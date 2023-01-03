package com.example.edubox.repository;

import com.example.edubox.constant.ECommonStatus;
import com.example.edubox.entity.Presentation;
import com.example.edubox.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question,Integer> {

    @Query("SELECT q FROM Question q WHERE q.status = :status AND " +
            " ( q.presentation = :presentation OR :presentation IS NULL ) AND " +
            " ( q.isAnswered = :isAnswered OR :isAnswered IS NULL ) AND " +
            " ( q.vote = :vote OR :vote IS NULL ) AND " +
            " ( q.postDate >= :from OR :from IS NULL ) AND " +
            " ( q.postDate <= :to OR :to IS NULL )  ")
    List<Question> getQuestions(Presentation presentation,
                                Boolean isAnswered,
                                Integer vote,
                                LocalDateTime from,
                                LocalDateTime to,
                                ECommonStatus status);
    Optional<Question> findQuestionByCode(String code);
}
