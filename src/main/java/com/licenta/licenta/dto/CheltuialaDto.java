package com.licenta.licenta.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
public class CheltuialaDto {
    @Getter @Setter
    private Long id;
    @Getter @Setter
    private String nume_cheltuiala;
    @Getter @Setter
    private Long calcul_id;
    @Getter @Setter
    private String calcul_nume;
    @Getter @Setter
    private Date data_introducere;
    @Getter @Setter
    private double suma;
    @Getter @Setter
    private Integer numar_factura;
    @Getter @Setter
    private String serie_factura;

}
