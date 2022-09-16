package com.licenta.licenta.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="Calcule_Cheltuieli")
@NoArgsConstructor
@AllArgsConstructor
public class CalculCheltuiala {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Long id;
    @Column(nullable = false)
    @Getter @Setter
    private String nume;
    @Column(nullable = false)
    @Getter @Setter
    private Integer formula_calcul;


   public CalculCheltuiala(Long id){
        this.id = id;
    }
}
