package com.example.edubox.entity;

import com.example.edubox.constant.ECommonStatus;
import com.example.edubox.constant.EGender;
import lombok.Getter;
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
@Table(name = "user")
public class User implements Serializable {
    private static final long serialVersionUID = -4917850556927078372L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "code")
    private String code;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "email")
    private String email;

    @Column(name = "gender")
    @Convert(converter = EGender.Converter.class)
    private EGender gender;

    @Column(name = "age")
    private Integer age;

    @Column(name = "is_enabled")
    private Boolean isEnabled = Boolean.FALSE;

    @Column(name = "role")
    private String role;

    @Column(name = "status")
    @Convert(converter = ECommonStatus.Converter.class)
    private ECommonStatus status;
}
