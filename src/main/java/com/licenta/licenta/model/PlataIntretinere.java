package com.licenta.licenta.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="plati_intretinere")
@NoArgsConstructor
@AllArgsConstructor
public class PlataIntretinere {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apartament_id", referencedColumnName = "id")
    @Getter @Setter
    private Apartament apartament;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @Getter @Setter
    private User user;
    @Column(nullable = false)
    @Getter @Setter
    private Integer an;
    @Column(nullable = false)
    @Getter @Setter
    private Integer luna;
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    @Getter @Setter
    private Date data_intocmire;
    @Column(nullable = false)
    @Getter @Setter
    private Double suma;
    @Column(nullable = false)
    @Getter @Setter
    private Integer achitatComplet;
//    @Column(nullable = false, columnDefinition="TEXT")
//    @Getter @Setter
//    private String details;
    @Column()
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "plataIntretinere")
    @Getter @Setter
    private List<Chitanta> chitante = new ArrayList<>();
    @Column()
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "plataIntretinere")
    @Getter @Setter
    private List<PlataIntretinereDetalii> plataIntretinereDetaliiList = new ArrayList<>();

    public PlataIntretinere(Long id, Apartament apartament, User user, Integer an, Integer luna, Date data_intocmire, Double suma, Integer achitatComplet) {
        this.id = id;
        this.apartament = apartament;
        this.user = user;
        this.an = an;
        this.luna = luna;
        this.data_intocmire = data_intocmire;
        this.suma = suma;
        this.achitatComplet = achitatComplet;
    }

}
