package com.example.edubox.model.req;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AddSlidesReq {
    private String presentCode;
    private List<SlideReq> slides;
}
