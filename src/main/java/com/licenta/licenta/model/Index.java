package com.licenta.licenta.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="Indecsi")
@NoArgsConstructor
@AllArgsConstructor
public class Index {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contor_id", referencedColumnName = "id")
    @Getter @Setter
    private Contor contor;
    @Column(nullable = false)
    @Getter @Setter
    private Double valoare_index;
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    @Getter @Setter
    private Date data_citire;
    @Column(nullable = false)
    @Getter @Setter
    private boolean autocitit;

    public Index(Double valoare_index) {
        this.valoare_index = valoare_index;
    }
    public Index(Double valoare_index, boolean autocitit) {
        this.valoare_index = valoare_index;
        this.autocitit = autocitit;
    }
}
