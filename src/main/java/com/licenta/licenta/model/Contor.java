package com.licenta.licenta.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Contoare")
@NoArgsConstructor
@AllArgsConstructor
public class Contor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;
    @Column(nullable = false)
    @Getter @Setter
    private String nume_contor;
    @Column(nullable = false)
    @Getter @Setter
    private boolean contor_general;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "serviciu_id", referencedColumnName = "id")
    @Getter @Setter
    private Serviciu serviciu;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "apartament_id", referencedColumnName = "id")
    @Getter @Setter
    private Apartament apartament;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "contor")
    @Getter @Setter
    List<Index> indecsi = new ArrayList<>();

    public Contor(Long id, String nume_contor, boolean contor_general, Serviciu serviciu, Apartament apartament) {
        this.id = id;
        this.nume_contor = nume_contor;
        this.contor_general = contor_general;
        this.serviciu = serviciu;
        this.apartament = apartament;
    }

    public Contor(Long id, String nume_contor, boolean contor_general, Serviciu serviciu) {
        this.id = id;
        this.nume_contor = nume_contor;
        this.contor_general = contor_general;
        this.serviciu = serviciu;
    }
}
