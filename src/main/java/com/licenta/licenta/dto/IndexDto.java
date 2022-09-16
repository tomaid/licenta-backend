package com.licenta.licenta.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@NoArgsConstructor
@AllArgsConstructor
public class IndexDto {
    @Getter @Setter
    private Long id;
    @Getter @Setter
    private String contorId;
    @Getter @Setter
    private double valoareIndex;
    @Getter @Setter
    private Date dataCitire;
    @Getter @Setter
    private boolean autocitit;
    @Getter @Setter
    private double consum;
}
