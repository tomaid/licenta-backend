package com.licenta.licenta.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class ContorIndexDto {
    @Getter
    @Setter
    private Long id;
    @Getter @Setter
    private String numeServiciu;
    @Getter @Setter
    private Long idServiciu;
    @Getter @Setter
    private String numeContor;
    @Getter @Setter
    private double ultimulIndex;
    @Getter @Setter
    private double indexCurent;
}
