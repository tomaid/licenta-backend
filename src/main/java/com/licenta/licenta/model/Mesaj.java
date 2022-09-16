package com.licenta.licenta.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class Mesaj {
    @Getter
    @Setter
    Integer code;
    @Getter
    @Setter
    String message;
    @Getter
    @Setter
    Long asociatie;

}
