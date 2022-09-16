package com.licenta.licenta.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class ConsumIndividualPentruFactura {
    @Getter
    @Setter
    private Long serviciuId;
    @Getter @Setter
    private Long apartamentId;
    @Getter @Setter
    private double valoareIndex;

}
