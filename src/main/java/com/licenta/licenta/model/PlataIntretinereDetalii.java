package com.licenta.licenta.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="plati_intretinere_detalii")
@NoArgsConstructor
@AllArgsConstructor
public class PlataIntretinereDetalii {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "plata_intretinere_id", referencedColumnName = "id")
    @Getter @Setter
    private PlataIntretinere plataIntretinere;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "serviciu_id", referencedColumnName = "id")
    @Getter @Setter
    private Serviciu serviciu;
    @Column(nullable = false)
    @Getter @Setter
    private double suma;
    @Column(nullable = false)
    @Getter @Setter
    private double consum;
    @Column(nullable = false)
    @Getter @Setter
    private String textDetalii;
    @Transient
    @Getter @Setter
    private Long apartmentId;

    public PlataIntretinereDetalii(Long id, double suma, String textDetalii) {
        this.id = id;
        this.suma = suma;
        this.textDetalii = textDetalii;
    }

    public PlataIntretinereDetalii(Long id, double suma, String textDetalii, double consum) {
        this.id = id;
        this.suma = suma;
        this.consum = consum;
        this.textDetalii = textDetalii;
    }
}
