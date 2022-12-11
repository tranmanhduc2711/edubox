package com.example.edubox.entity;

import com.example.edubox.constant.ECommonStatus;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "slide_choice")
@Data
public class SlideChoice implements Serializable {
    private static final long serialVersionUID = -2314561211277531581L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(targetEntity = Slide.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "slide_id")
    private Slide slide;

    @Column(name = "icon")
    private String icon;

    @Column(name = "is_correct")
    private Boolean isCorrect;

    @Column(name = "status")
    @Convert(converter = ECommonStatus.Converter.class)
    private ECommonStatus status;
}
