package com.example.edubox.service.impl;

import com.example.edubox.constant.ECommonStatus;
import com.example.edubox.constant.EPresentType;
import com.example.edubox.constant.ErrorCode;
import com.example.edubox.entity.Collaborator;
import com.example.edubox.entity.Group;
import com.example.edubox.entity.Presentation;
import com.example.edubox.entity.User;
import com.example.edubox.exception.BusinessException;
import com.example.edubox.model.req.CreatePresentationReq;
import com.example.edubox.model.req.UpdatePresentationReq;
import com.example.edubox.model.res.PresentationRes;
import com.example.edubox.repository.CollaboratorRepository;
import com.example.edubox.repository.PresentationRepository;
import com.example.edubox.service.GroupService;
import com.example.edubox.service.PresentationService;
import com.example.edubox.service.SequenceService;
import com.example.edubox.service.UserService;
import com.example.edubox.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PresentationServiceImpl implements PresentationService {
    private static final String PRESENTATION_CODE = "presentation-code-";
    @Autowired
    private PresentationRepository presentationRepository;
    @Autowired
    private CollaboratorRepository collaboratorRepository;
    @Autowired
    private GroupService groupService;
    @Autowired
    private UserService userService;

    @Autowired
    private SequenceService sequenceService;

    @Override
    public List<PresentationRes> getPresentations(EPresentType type, String code, String groupCode) {
        List<Presentation> presentations= presentationRepository.findPresentations(type,code,groupCode);
        if(CollectionUtils.isEmpty(presentations)){
            return null;
        } else {
            return presentations.stream()
                    .map(PresentationRes::valueOf)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public PresentationRes create(CreatePresentationReq req) {
        Group group = groupService.findActiveGroup(req.getGroupCode());
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUsername(principal);

        Presentation presentation = new Presentation();
        presentation.setCode(buildPresentationCode());
        presentation.setPresentType(EPresentType.PUBLIC);
        if(req.getType() != null) {
            presentation.setPresentType(req.getType());
        }
        presentation.setName(req.getName());
        presentation.setDescription(req.getDescription());
        presentation.setGroup(group);
        presentation.setHost(user);
        presentation.setTotalSlide(0);
        presentation.setIsRunning(Boolean.FALSE);
        presentation.setStatus(ECommonStatus.ACTIVE);
        presentationRepository.save(presentation);
        return PresentationRes.valueOf(presentation);
    }

    @Override
    public Presentation update(UpdatePresentationReq req) {
        Optional<Presentation> record = presentationRepository.findByCode(req.getCode());
        if (record.isEmpty()) {
            throw new BusinessException(ErrorCode.PRESENTATION_CODE_NOT_FOUND, "Presentation code not found");
        }
        Presentation presentation = record.get();
        presentation.setName(req.getName());
        if(req.getDescription() != null) {
            presentation.setDescription(req.getDescription());
        }
        if(req.getType() != null){
            presentation.setPresentType(req.getType());
        }

        presentationRepository.save(presentation);
        return presentation;
    }

    @Override
    public PresentationRes delete(String presentCode) {
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUsername(principal);

        Presentation presentation = findActive(presentCode);
        if(!presentation.getHost().getUsername().equals(user.getUsername())){
            throw new BusinessException(ErrorCode.ACCESS_DENIED,"Only host& collaborator cant delete");
        }
        presentation.setStatus(ECommonStatus.INACTIVE);
        presentationRepository.save(presentation);
        return PresentationRes.valueOf(presentation);
    }

    @Override
    public Presentation findActive(String code) {
        Presentation presentation = presentationRepository.findByCode(code).orElseThrow(
                () -> new BusinessException(ErrorCode.PRESENTATION_CODE_NOT_FOUND, String.format("Presentation code not found: %s", code))
        );
        if (ECommonStatus.INACTIVE.equals(presentation.getStatus())) {
            throw new BusinessException(ErrorCode.PRESENTATION_CODE_INACTIVE, "Presentation inactive");
        }
        return presentation;
    }

    @Override
    public Boolean checkIsHost(String presentCode) {
        Presentation presentation = findActive(presentCode);
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUsername(principal);
        return user.getUsername().equals(presentation.getHost().getUsername());
    }

    @Override
    public void start(String presentCode) {
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUsername(principal);

        Presentation presentation = findActive(presentCode);
        checkPresentPermission(presentation,user);
        presentation.setIsRunning(Boolean.TRUE);
        presentationRepository.save(presentation);
    }

    @Override
    public void end(String presentCode) {
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUsername(principal);

        Presentation presentation = findActive(presentCode);
        checkPresentPermission(presentation,user);
        presentation.setIsRunning(Boolean.FALSE);
        presentationRepository.save(presentation);
    }

    private void checkPresentPermission(Presentation presentation,User user){
        boolean isDenied = false;
        List<Collaborator> collaborators = collaboratorRepository.getCollaboratorByPresentationAndStatus(presentation,ECommonStatus.ACTIVE);
        isDenied = collaborators
                .stream()
                .noneMatch(item -> (item.getCollaborator().getUsername().equals(user.getUsername())));
        if(presentation.getHost().getUsername().equals(user.getUsername())) {
            isDenied = false;
        }
        if(isDenied) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED,"Access denied");
        }
    }


    private String buildPresentationCode() {
        int yy = LocalDate.now().getYear() % 100;
        int nextSeq = sequenceService.getNextSeq(PRESENTATION_CODE, yy);
        String seqVal = Strings.formatWithZeroPrefix(nextSeq, 4);

        return String.format("%s%s%s", "PR", yy, seqVal);
    }
}
