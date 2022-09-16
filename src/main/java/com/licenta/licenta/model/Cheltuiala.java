package com.licenta.licenta.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name="Cheltuieli")
@NoArgsConstructor
@AllArgsConstructor
public class Cheltuiala {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asociatie_id", referencedColumnName = "id")
    @Getter @Setter
    private Asociatie asociatie;
    @Column(nullable = false)
    @Getter @Setter
    private String nume_cheltuiala;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calcul_id", referencedColumnName = "id")
    @Getter @Setter
    private CalculCheltuiala calculCheltuiala;
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    @Getter @Setter
    private Date data_introducere;
    @Column(nullable = false)
    @Getter @Setter
    private double suma;
    @Column(nullable = false)
    @Getter @Setter
    private Integer numar_factura;
    @Column(nullable = false)
    @Getter @Setter
    private String serie_factura;

}
