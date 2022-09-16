package com.licenta.licenta.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="Apartamente")
@NoArgsConstructor
@AllArgsConstructor
public class Apartament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Long id;
    @Column(nullable = false)
    @Getter @Setter
    private String numar_apartament;
    @Column(nullable = false)
    @Getter @Setter
    private Double suprafata_mp;
    @Column(nullable = false)
    @Getter @Setter
    private Integer numar_locatari;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asociatie_id", referencedColumnName = "id")
    @Getter @Setter
    private Asociatie asociatie;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @Getter @Setter
    private User user;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "apartament")
    @Getter @Setter
    List<Contor> contoare;

    public Apartament(Long id, String numar_apartament, Double suprafata_mp, Integer numar_locatari, Asociatie asociatie, User user) {
        this.id = id;
        this.numar_apartament = numar_apartament;
        this.suprafata_mp = suprafata_mp;
        this.numar_locatari = numar_locatari;
        this.asociatie = asociatie;
        this.user = user;
    }
}
