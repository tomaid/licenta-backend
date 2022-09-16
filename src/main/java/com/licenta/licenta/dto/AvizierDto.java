package com.licenta.licenta.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
public class AvizierDto {
    @Getter @Setter
    private String numarApartament;
    @Getter @Setter
    private Integer numarLocatari;
    @Getter @Setter
    private double suprafataApartament;
    @Getter @Setter
    private List<AvizierRandDto> avizierRandDtoList;
    @Getter @Setter
    private double sumaDePlatit;
    @Getter @Setter
    private double cpi;



}
