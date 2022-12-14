package com.example.edubox.model.req;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UpdateSlideReq {
    private String presentCode;

    private List<SlideReq> slides;
}
