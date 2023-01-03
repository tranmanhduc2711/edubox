package com.example.edubox.entity;

import com.example.edubox.constant.ECommonStatus;
import com.example.edubox.constant.ESlideType;
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

    @Column(name = "item_no")
    private Integer itemNo;

    @Column(name = "heading")
    private String heading;

    @Column(name = "paragraph")
    private String paragraph;

    @Column(name = "type")
    private ESlideType slideType;

    @OneToOne(targetEntity = Presentation.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "present_id")
    private Presentation presentation;

    @Column(name = "timer")
    private Integer timer;

    @Column(name = "status")
    @Convert(converter = ECommonStatus.Converter.class)
    private ECommonStatus status = ECommonStatus.ACTIVE;
}
