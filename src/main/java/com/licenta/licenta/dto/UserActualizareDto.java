package com.licenta.licenta.dto;

import com.licenta.licenta.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class UserActualizareDto {
    @Setter @Getter
    private String user;
    @Setter @Getter
    private String nume;
    @Setter @Getter
    private String prenume;
    @Setter @Getter
    private String telefon;

}
