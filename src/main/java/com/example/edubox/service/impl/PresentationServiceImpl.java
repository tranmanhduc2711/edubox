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
import com.example.edubox.service.SequenceService;
import com.example.edubox.service.UserService;
import com.example.edubox.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
    private GroupService groupService;
    @Autowired
    private UserService userService;

    @Autowired
    private SequenceService sequenceService;

    @Override
    public List<PresentationRes> getPresentations(EPresentType type, String code) {

        return presentationRepository.findPresentations(type, code)
                .stream()
                .map(PresentationRes::valueOf)
                .collect(Collectors.toList());
    }

    @Override
    public PresentationRes create(CreatePresentationReq req) {
        Group group = groupService.findActiveGroup(req.getGroupCode());
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUsername(principal);

        Presentation presentation = new Presentation();
        presentation.setCode(buildPresentationCode());
        presentation.setPresentType(req.getType());
        presentation.setName(req.getName());
        presentation.setDescription(req.getDescription());
        presentation.setGroup(group);
        presentation.setHost(user);
        presentation.setTotalSlide(0);
        presentation.setStatus(ECommonStatus.ACTIVE);
        presentationRepository.save(presentation);
        return PresentationRes.valueOf(presentation);
    }

    @Override
    public PresentationRes update(UpdatePresentationReq req) {
        Optional<Presentation> record = presentationRepository.findByCode(req.getCode());
        if (record.isEmpty()) {
            throw new BusinessException(ErrorCode.PRESENTATION_CODE_NOT_FOUND, "Presentation code not found");
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

    private String buildPresentationCode() {
        int yy = LocalDate.now().getYear() % 100;
        int nextSeq = sequenceService.getNextSeq(PRESENTATION_CODE, yy);
        String seqVal = Strings.formatWithZeroPrefix(nextSeq, 4);

        return String.format("%s%s%s", "PR", yy, seqVal);
    }
}
