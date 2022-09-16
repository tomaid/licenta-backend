package com.licenta.licenta.model;

import javax.persistence.*;

@Entity
@Table(name="Judete")
public class Judet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String nume;

    public Judet() {
    }
    public Judet(Long id, String nume) {
        this.id = id;
        this.nume = nume;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }
}
