package com.licenta.licenta.model;
import com.licenta.licenta.model.Judet;
import javax.persistence.*;

@Entity
@Table(name="Localitati")
public class Localitate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nume;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "judet_id", referencedColumnName = "id")
    private Judet judet;

    public Localitate() {
    }
    public Localitate(Long id, String nume, Judet judet) {
        this.id = id;
        this.nume = nume;
        this.judet = judet;
    }
    public Localitate(Long id, String nume) {
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

    public Judet getJudet() {
        return judet;
    }

    public void setJudet(Judet judet) {
        this.judet = judet;
    }
}
