package com.licenta.licenta.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @AllArgsConstructor
public class UserActualizareParolaDto {
    @Getter @Setter
    private String parolaVeche;
    @Getter @Setter
    private String parolaNoua;
}
