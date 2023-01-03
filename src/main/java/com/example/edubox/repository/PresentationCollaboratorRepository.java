package com.example.edubox.repository;

import com.example.edubox.entity.Collaborator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PresentationCollaboratorRepository extends JpaRepository<Collaborator,Integer> {
}
