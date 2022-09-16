package com.licenta.licenta.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class GenerareFacturi {
    @Getter @Setter
    private int method;
    @Getter @Setter
    private int luna;
    @Getter @Setter
    private int an;
}
