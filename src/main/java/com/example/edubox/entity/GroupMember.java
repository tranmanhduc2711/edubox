package com.example.edubox.entity;

import com.example.edubox.constant.ECommonStatus;
import com.example.edubox.constant.ERoleType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "group_member")
@Setter
@Getter
public class GroupMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name="group_id")
    private Group group;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "type")
    @Convert(converter = ERoleType.Converter.class)
    private ERoleType roleType;

    @Column(name = "status")
    @Convert(converter = ECommonStatus.Converter.class)
    private ECommonStatus status;
}
