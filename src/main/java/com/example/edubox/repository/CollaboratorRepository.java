package com.example.edubox.repository;

import com.example.edubox.constant.ECommonStatus;
import com.example.edubox.entity.Collaborator;
import com.example.edubox.entity.Presentation;
import com.example.edubox.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CollaboratorRepository extends JpaRepository<Collaborator, Integer> {
    @Query("SELECT c FROM Collaborator c WHERE c.collaborator = :user" +
            " AND c.presentation = :presentation " +
            " AND c.status = :status")
    Optional<Collaborator> findCollaboratorByUser(User user, Presentation presentation, ECommonStatus status);

    List<Collaborator> getCollaboratorByPresentationAndStatus(Presentation presentation, ECommonStatus status);
}
