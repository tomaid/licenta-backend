package com.licenta.licenta.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor  @AllArgsConstructor
public class ApartamentDto {
    @Getter @Setter
    private Long id;
    @Getter @Setter
    private String numar;
    @Getter @Setter
    private double suprafataMp;
    @Getter @Setter
    private Integer numarLocatari;
    @Getter @Setter
    private Long asociatieId;
    @Getter @Setter
    private Long userId;
    @Getter @Setter
    private String prenume;
    @Getter @Setter
    private String nume;
    @Getter @Setter
    private String telefon;
    @Getter @Setter
    private String email;
}
