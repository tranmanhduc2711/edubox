package com.example.edubox.service.impl;

import com.example.edubox.constant.ECommonStatus;
import com.example.edubox.constant.ErrorCode;
import com.example.edubox.entity.Collaborator;
import com.example.edubox.entity.Presentation;
import com.example.edubox.entity.User;
import com.example.edubox.exception.BusinessException;
import com.example.edubox.repository.CollaboratorRepository;
import com.example.edubox.service.CollaboratorService;
import com.example.edubox.service.PresentationService;
import com.example.edubox.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CollaboratorServiceImpl implements CollaboratorService {
    private final UserService userService;
    private final PresentationService presentationService;
    private final CollaboratorRepository collaboratorRepository;

    @Override
    public void addCollaborator(String userCode, String presentCode) {
        User user = userService.findActiveUser(userCode);
        Presentation presentation = presentationService.findActive(presentCode);

        Collaborator collaborator = new Collaborator();
        collaborator.setCollaborator(user);
        collaborator.setPresentation(presentation);
        collaborator.setStatus(ECommonStatus.ACTIVE);

        collaboratorRepository.save(collaborator);
    }

    @Override
    public Boolean checkACL(String presentCode) {
        Presentation presentation = presentationService.findActive(presentCode);
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUsername(principal);

        Optional<Collaborator> collaborator = collaboratorRepository.findCollaboratorByUser(user,presentation,ECommonStatus.ACTIVE);
        if(collaborator.isPresent()) {
            return true;
        }
        return false;
    }
}
