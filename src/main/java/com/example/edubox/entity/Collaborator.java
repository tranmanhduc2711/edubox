package com.example.edubox.entity;

import com.example.edubox.constant.ECommonStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "presentation_collaborator")
@Getter
@Setter
public class Collaborator implements Serializable {
    private static final long serialVersionUID = 7074550961915691798L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User collaborator;

    @OneToOne(targetEntity = Presentation.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "presentation_id")
    private Presentation presentation;

    @Column(name = "status")
    @Convert(converter = ECommonStatus.Converter.class)
    private ECommonStatus status = ECommonStatus.ACTIVE;
}
