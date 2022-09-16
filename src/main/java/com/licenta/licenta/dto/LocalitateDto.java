package com.licenta.licenta.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class LocalitateDto {
    @Getter
    @Setter
    private Long id;
    @Getter @Setter
    private String nume;
}
