package com.licenta.licenta.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="istoric_pret")
@NoArgsConstructor
@AllArgsConstructor
public class IstoricPret {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "serviciu_id", referencedColumnName = "id")
    @Getter @Setter
    private Serviciu serviciu;
    @Getter @Setter
    @Column(nullable = false)
    private double pret;
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Getter @Setter
    private Date data_pret;



}
