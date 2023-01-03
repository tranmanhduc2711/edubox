package com.example.edubox.service;

import com.example.edubox.entity.User;

import java.util.List;

public interface CollaboratorService {

    List<User> getPresentCollaborators(String presentCode);

    void addCollaborator(String userCode, String presentCode);

    void deleteCollaborator(String email, String presentCode);

    Boolean checkACL(String presentCode);
}
