package com.licenta.licenta.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class ServiciuDto {
    @Getter
    @Setter
    private Long id;
    @Getter @Setter
    private String Nume;
    @Getter @Setter
    private double pret;
    @Getter @Setter
    private Long asociatie_id;
    @Getter @Setter
    private double consumLunar;
}
