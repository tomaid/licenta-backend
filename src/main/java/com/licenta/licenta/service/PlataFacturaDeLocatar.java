package com.licenta.licenta.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class PlataFacturaDeLocatar {
    @Getter @Setter
    private String card;
    @Getter @Setter
    private Integer lunaExpirare;
    @Getter @Setter
    private Integer anExpirare;
    @Getter @Setter
    private Integer cvx;
    @Getter @Setter
    private double valoare;
}
