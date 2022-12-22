package com.example.edubox.repository;

import com.example.edubox.constant.ECommonStatus;
import com.example.edubox.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group,Integer> {
    Optional<Group> findByGroupCode(String code);

    @Query("SELECT g FROM Group g WHERE g.status =:status")
    List<Group> getListGroup(ECommonStatus status);
}
