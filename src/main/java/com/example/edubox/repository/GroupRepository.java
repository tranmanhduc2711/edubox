package com.example.edubox.repository;

import com.example.edubox.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group,Integer> {
    Optional<Group> findByGroupCode(String code);

}
