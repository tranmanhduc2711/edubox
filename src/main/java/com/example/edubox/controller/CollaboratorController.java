package com.example.edubox.controller;

import com.example.edubox.controller.base.BaseController;
import com.example.edubox.model.req.AddCollaboratorReq;
import com.example.edubox.model.req.DeleteCollabReq;
import com.example.edubox.service.CollaboratorService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/collaborator")
@AllArgsConstructor
public class CollaboratorController extends BaseController {

    private final CollaboratorService collaboratorService;

    @GetMapping("")
    ResponseEntity<?> getPresentCollaborators(@RequestParam(value = "presentCode",required = true) String presentCode){
        return success(collaboratorService.getPresentCollaborators(presentCode));
    }
    @PostMapping("/add")
    ResponseEntity<?> add(@RequestBody AddCollaboratorReq req){
        collaboratorService.addCollaborator(req.getEmail(),req.getPresentCode());
        return success(null);
    }

    @PostMapping("/delete")
    ResponseEntity<?> delete(@RequestBody DeleteCollabReq req){
        collaboratorService.deleteCollaborator(req.getEmail(),req.getPresentCode());
        return success(null);
    }
    @GetMapping("/check-acl")
    ResponseEntity<?> checkPermission(@RequestParam(value = "presentCode",required = true) String presentCode){
        return success(collaboratorService.checkACL(presentCode));
    }
}
