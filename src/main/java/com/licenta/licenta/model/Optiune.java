package com.licenta.licenta.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="Optiuni")
@NoArgsConstructor
@AllArgsConstructor
public class Optiune {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;
    @Column(nullable = false)
    @Getter @Setter
    private String nume_optiune;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "categorie_id", referencedColumnName = "id")
    @Getter @Setter
    private Categorie categorie;
    @ManyToMany
    @JoinTable(
            name = "optiuni_dispozitive",
            joinColumns = @JoinColumn(name = "optiune_id"),
            inverseJoinColumns = @JoinColumn(name = "dispozitiv_id"))
    Set<Dispozitiv> optiuneDispozitiv = new HashSet<>();
}
