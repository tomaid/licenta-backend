package com.licenta.licenta.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="Dispozitive")
@NoArgsConstructor
@AllArgsConstructor
public class Dispozitiv {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Long id;
    @Column(nullable = false)
    @Getter @Setter
    private String model_dispozitiv;
    @Column(nullable = false)
    @Getter @Setter
    private String producator;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "clasa_energetica_id", referencedColumnName = "id")
    @Getter @Setter
    private ClasaEnergetica clasaEnergetica;
    @Column(nullable = false)
    @Getter @Setter
    private Integer an_fabricatie;
    @Column(nullable = false)
    @Getter @Setter
    private double pret;
    @Column(nullable = false)
    @Getter @Setter
    private double consum;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categorie_id", referencedColumnName = "id")
    @Getter @Setter
    private Categorie categorie;
    @ManyToMany
    @JoinTable(
            name = "optiuni_dispozitive",
            joinColumns = @JoinColumn(name = "dispozitiv_id"),
            inverseJoinColumns = @JoinColumn(name = "optiune_id"))
    Set<Optiune> dispozitivOptiune = new HashSet<>();
    public Dispozitiv(Long id, String model_dispozitiv, double pret, double consum, Categorie categorie) {
        this.id = id;
        this.model_dispozitiv = model_dispozitiv;
        this.pret = pret;
        this.consum = consum;
        this.categorie = categorie;
    }
}

