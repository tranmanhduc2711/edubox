package com.example.edubox.entity;

import com.example.edubox.constant.ECommonStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Setter
@Getter
@Entity
@Table(name = "edu_group")
@NoArgsConstructor
public class  Group implements Serializable {
    private static final long serialVersionUID = 3056572664881306837L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "group_code")
    private String groupCode;

    @Column(name = "group_description")
    private String description;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "total_member")
    private Integer totalMember;

    @Column(name = "status")
    @Convert(converter = ECommonStatus.Converter.class)
    private ECommonStatus status=ECommonStatus.ACTIVE;

    public void incr(int val) {
        this.totalMember += val;
    }
}
