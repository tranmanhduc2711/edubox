package com.example.edubox.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "seq_next")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Sequence {

    @Id
    @Column(name = "seq_name")
    private String seqName;

    @Column(name = "cur_val")
    private Integer curVal;

    public void incr(int val) {
        this.curVal += val;
    }
}
