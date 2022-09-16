package com.licenta.licenta.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="Chitante")
@NoArgsConstructor
@AllArgsConstructor
public class Chitanta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Long id;
    @Column(nullable = false)
    @Getter @Setter
    private Integer numar_chitanta;
    @Column(nullable = false)
    @Getter @Setter
    private String serie_chitanta;
    @Column(nullable = false)
    @Getter @Setter
    private String detalii;
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    @Getter @Setter
    private Date data_achitare;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plata_intretinere_id", referencedColumnName = "id")
    @Getter @Setter
    private PlataIntretinere plataIntretinere;
    @Column(nullable = false)
    @Getter @Setter
    private Double suma;

    public Chitanta(Long id, String serie_chitanta, String detalii, Date data_achitare, Double suma) {
        this.id = id;
        this.serie_chitanta = serie_chitanta;
        this.detalii = detalii;
        this.data_achitare = data_achitare;
        this.suma = suma;
    }
}
