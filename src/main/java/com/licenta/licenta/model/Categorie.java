package com.licenta.licenta.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="Categorii")
@NoArgsConstructor
@AllArgsConstructor
public class Categorie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;
    @Column(nullable = false)
    @Getter @Setter
    private String nume_categorie;
}
