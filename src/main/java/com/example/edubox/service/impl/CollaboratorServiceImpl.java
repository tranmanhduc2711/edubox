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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CollaboratorServiceImpl implements CollaboratorService {
    private final UserService userService;
    private final PresentationService presentationService;
    private final CollaboratorRepository collaboratorRepository;

    @Override
    public List<User> getPresentCollaborators(String presentCode) {
        Presentation presentation = presentationService.findActive(presentCode);
        List<Collaborator> collaborators = collaboratorRepository.getCollaboratorByPresentationAndStatus(presentation, ECommonStatus.ACTIVE);
        List<User> resultList = collaborators.stream().map(Collaborator::getCollaborator).collect(Collectors.toList());
        return resultList;
    }

    @Override
    public void addCollaborator(String email, String presentCode) {
        User user = userService.findByUsername(email);
        Presentation presentation = presentationService.findActive(presentCode);

        Optional<Collaborator> record = collaboratorRepository.findCollaboratorByUser(user, presentation, ECommonStatus.ACTIVE);
        if (record.isPresent()) {
            throw new BusinessException(ErrorCode.COLLABORATOR_IS_ALREADY_EXIST, "collab is already exist");
        }
        Collaborator collaborator = new Collaborator();
        collaborator.setCollaborator(user);
        collaborator.setPresentation(presentation);
        collaborator.setStatus(ECommonStatus.ACTIVE);

        collaboratorRepository.save(collaborator);
    }

    @Override
    public void deleteCollaborator(String email, String presentCode) {
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User host = userService.findByUsername(principal);

        Presentation presentation = presentationService.findActive(presentCode);
        if (!host.getUsername().equals(presentation.getHost().getUsername())) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED, "missing permission");
        }

        User user = userService.findByUsername(email);
        Optional<Collaborator> collaborator = collaboratorRepository.findCollaboratorByUser(user, presentation, ECommonStatus.ACTIVE);
        if (collaborator.isPresent()) {
            collaborator.get().setStatus(ECommonStatus.INACTIVE);
            collaboratorRepository.save(collaborator.get());
        }
    }

    @Override
    public Boolean checkACL(String presentCode) {
        Presentation presentation = presentationService.findActive(presentCode);
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUsername(principal);

        Optional<Collaborator> collaborator = collaboratorRepository.findCollaboratorByUser(user, presentation, ECommonStatus.ACTIVE);
        if (collaborator.isPresent()) {
            return true;
        }
        return false;
    }
}
