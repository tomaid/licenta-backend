package com.licenta.licenta.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class ContorIndex {
    @Getter @Setter
    private Long contorId;
    @Getter @Setter
    private Long serviciuId;
    @Getter @Setter
    private double indexCurent;
    @Getter @Setter
    private double indexTrecut;
    @Getter @Setter
    private double pret;
}
