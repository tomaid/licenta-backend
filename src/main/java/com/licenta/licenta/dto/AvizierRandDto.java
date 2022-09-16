package com.licenta.licenta.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class AvizierRandDto {
    @Getter @Setter
    private double consum;
    @Getter @Setter
    private double suma;
    @Getter @Setter
    private String text;
}
