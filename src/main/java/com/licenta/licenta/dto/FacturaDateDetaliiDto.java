package com.licenta.licenta.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class FacturaDateDetaliiDto {
        @Getter @Setter
        private Long id;
        @Getter @Setter
        private double valoare;
        @Getter @Setter
        private String detalii;
}
