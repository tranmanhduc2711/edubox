package com.example.edubox.model.res;

import com.example.edubox.constant.ECommonStatus;
import com.example.edubox.entity.Group;
import com.example.edubox.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupRes {
    private String name;
    private String code;
    private String description;
    private ECommonStatus status;
    private UserRes owner;

    public static GroupRes valueOf(Group group,UserRes owner) {
        return GroupRes.builder()
                .code(group.getGroupCode())
                .name(group.getGroupName())
                .description(group.getDescription())
                .status(group.getStatus())
                .owner(owner)
                .build();
    }

}
