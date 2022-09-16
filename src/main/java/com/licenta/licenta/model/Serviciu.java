package com.licenta.licenta.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="Servicii")
@NoArgsConstructor
@AllArgsConstructor
public class Serviciu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Long id;
    @Column(nullable = false)
    @Getter @Setter
    private String nume_serviciu;
    @Column(nullable = false)
    @Getter @Setter
    private Double pret_serviciu;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asociatie_id", referencedColumnName = "id")
    @Getter @Setter
    private Asociatie asociatie;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "serviciu")
    @Getter @Setter
    List<Contor> contoare;

    public Serviciu(Long id, String nume_serviciu, Double pret_serviciu, Asociatie asociatie) {
        this.id = id;
        this.nume_serviciu = nume_serviciu;
        this.pret_serviciu = pret_serviciu;
        this.asociatie = asociatie;
    }

    public boolean findServiciuByAsociatie(Asociatie asociatie) {
        return this.asociatie.equals(asociatie);
    }

}
