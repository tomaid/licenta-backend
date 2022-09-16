package com.licenta.licenta.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
public class FacturaDateDto {
    @Getter @Setter
    private Long id;
    @Getter @Setter
    private String apartamentNumar;
    @Getter @Setter
    private String apartamentNume;
    @Getter @Setter
    private double valoare;
    @Getter @Setter
    private double valoareRestante;
    @Getter @Setter
    private double valoareAchitata;
    @Getter @Setter
    private Date dataIntocmire;
    @Getter @Setter
    private Date dataScadenta;
    @Getter @Setter
    private int luna;
    @Getter @Setter
    private int anul;
}
