package com.example.edubox.service;

public interface CollaboratorService {
    void addCollaborator(String userCode,String presentCode);

    Boolean checkACL(String presentCode);
}
