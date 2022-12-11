package com.example.edubox.entity;

import com.example.edubox.constant.ECommonStatus;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "slide")
@Data
public class Slide implements Serializable {
    private static final long serialVersionUID = 1910280619914407084L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "code")
    private String code;

    @Column(name = "item_no")
    private Integer itemNo;

    @Column(name = "question")
    private String question;

    @OneToOne(targetEntity = Presentation.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "present_id")
    private Presentation presentation;

    @Column(name = "status")
    @Convert(converter = ECommonStatus.Converter.class)
    private ECommonStatus status;
}
