package com.licenta.licenta.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @AllArgsConstructor
public class ContorDto {
    @Getter @Setter
    private Long id;
    @Getter @Setter
    private String nume;
    @Getter @Setter
    private Long idApartament;
    @Getter @Setter
    private String numeApartament;
    @Getter @Setter
    private boolean general;
    @Getter @Setter
    private Integer tip;
}
