package com.licenta.licenta.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@NoArgsConstructor
@AllArgsConstructor
public class AsociatieCreareDto {
    @Getter @Setter
    private String nume;
    @Getter @Setter
    private Long cif;
    @Getter @Setter
    private String autorizatie;
    @Getter @Setter
    private Long localitate;
    @Getter @Setter
    private String strada;
    @Getter @Setter
    private String numar;
    @Getter @Setter
    private String bloc;
    @Getter @Setter
    private String scara;
}
