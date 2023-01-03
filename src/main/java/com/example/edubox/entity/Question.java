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
import java.time.LocalDateTime;

@Entity
@Table(name = "question")
@Getter
@Setter
public class Question implements Serializable {
    private static final long serialVersionUID = -6809812398886058862L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "code")
    private String code;

    @Column(name = "title")
    private String title;

    @OneToOne(targetEntity = Presentation.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "presentation_id")
    private Presentation presentation;

    @Column(name = "is_answered")
    private Boolean isAnswered;

    @Column(name = "post_date")
    private LocalDateTime postDate;

    @Column(name = "vote")
    private Integer vote;

    @Column(name = "status")
    @Convert(converter = ECommonStatus.Converter.class)
    private ECommonStatus status = ECommonStatus.ACTIVE;

    public void incr(int val) {
        this.vote += val;
    }
}
