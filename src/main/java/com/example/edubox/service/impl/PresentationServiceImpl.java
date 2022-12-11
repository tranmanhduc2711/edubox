package com.example.edubox.service.impl;

import com.example.edubox.constant.ECommonStatus;
import com.example.edubox.constant.EPresentType;
import com.example.edubox.constant.ErrorCode;
import com.example.edubox.entity.Group;
import com.example.edubox.entity.Presentation;
import com.example.edubox.entity.User;
import com.example.edubox.exception.BusinessException;
import com.example.edubox.model.req.CreatePresentationReq;
import com.example.edubox.model.req.UpdatePresentationReq;
import com.example.edubox.model.res.PresentationRes;
import com.example.edubox.repository.PresentationRepository;
import com.example.edubox.service.GroupService;
import com.example.edubox.service.PresentationService;
import com.example.edubox.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PresentationServiceImpl implements PresentationService {
    @Autowired
    private PresentationRepository presentationRepository;
    @Autowired
    private GroupService groupService;
    @Autowired
    private UserService userService;

    @Override
    public List<Presentation> getPresentations(EPresentType type) {
        return presentationRepository.findPresentationByPresentType(type);
    }

    @Override
    public PresentationRes create(CreatePresentationReq req) {
        Group group = groupService.findActiveGroup(req.getGroupCode());
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUsername(principal);

        Presentation presentation = new Presentation();
        UUID uuid = UUID.randomUUID();
        presentation.setCode(uuid.toString());
        presentation.setPresentType(req.getType());
        presentation.setName(req.getName());
        presentation.setDescription(req.getDescription());
        presentation.setGroup(group);
        presentation.setHost(user);
        presentation.setStatus(ECommonStatus.ACTIVE);
        presentationRepository.save(presentation);
        return PresentationRes.valueOf(presentation);
    }

    @Override
    public PresentationRes update(UpdatePresentationReq req) {
        Optional<Presentation> record = presentationRepository.findByCode(req.getCode());
        if(record.isEmpty()) {
            throw new BusinessException(ErrorCode.PRESENTATION_CODE_NOT_FOUND,"Presentation code not found");
        }
        Presentation presentation = record.get();
        presentation.setName(req.getName());
        presentation.setDescription(req.getDescription());
        presentation.setPresentType(req.getType());
        presentation.setStatus(req.getStatus());

        presentationRepository.save(presentation);
        return PresentationRes.valueOf(presentation);
    }

    @Override
    public PresentationRes delete(String presentCode) {
        Presentation presentation = findActive(presentCode);
        presentation.setStatus(ECommonStatus.INACTIVE);
        presentationRepository.save(presentation);
        return PresentationRes.valueOf(presentation);
    }

    @Override
    public Presentation findActive(String code) {
        Presentation presentation = presentationRepository.findByCode(code).orElseThrow(
                () -> new BusinessException(ErrorCode.PRESENTATION_CODE_NOT_FOUND, String.format("Presentation code not found: %s", code))
        );
        if(ECommonStatus.INACTIVE.equals(presentation.getStatus())){
            throw  new BusinessException(ErrorCode.PRESENTATION_CODE_INACTIVE,"Presentation inactive");
        }
        return presentation;
    }
}
