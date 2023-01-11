package com.example.edubox.model.res;

import com.example.edubox.constant.ECommonStatus;
import com.example.edubox.entity.Group;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupRes {
    private String name;
    private String code;
    private String description;
    private Integer capacity;
    private ECommonStatus status;
    private int total;
    private UserRes owner;

    public static GroupRes valueOf(Group group,UserRes owner) {
        return GroupRes.builder()
                .code(group.getGroupCode())
                .name(group.getGroupName())
                .description(group.getDescription())
                .capacity(group.getCapacity())
                .status(group.getStatus())
                .total(group.getTotalMember())
                .owner(owner)
                .build();
    }

}
