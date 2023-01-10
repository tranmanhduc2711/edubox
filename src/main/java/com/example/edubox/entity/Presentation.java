package com.example.edubox.entity;

import com.example.edubox.constant.ECommonStatus;
import com.example.edubox.constant.EPresentType;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "presentation")
@Data
public class Presentation implements Serializable {

    private static final long serialVersionUID = -2140332237834500801L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "type")
    @Convert(converter = EPresentType.Converter.class)
    private EPresentType presentType;

    @OneToOne(targetEntity = Group.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "group_id")
    private Group group;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "host_id")
    private User host;

    @Column(name = "total_slide")
    private Integer totalSlide = 0;

    @Column(name = "is_running")
    private Boolean isRunning;

    @Column(name = "status")
    @Convert(converter = ECommonStatus.Converter.class)
    private ECommonStatus status = ECommonStatus.ACTIVE;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public void incr(int val) {
        this.totalSlide += val;
    }
    public void decr(int val) {
        this.totalSlide -= val;
    }
}
