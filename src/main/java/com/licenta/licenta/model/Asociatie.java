package com.licenta.licenta.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="Asociatii")
@NoArgsConstructor
@AllArgsConstructor
public class Asociatie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Long id;
    @Column(nullable = false, unique = true)
    @Getter @Setter
    private String nume;
    @Column(nullable = false, unique = true)
    @Getter @Setter
    private Long cif;
    @Column(nullable = false, unique = true)
    @Getter @Setter
    private String autorizatie;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "localitate_id", referencedColumnName = "id")
    @Getter @Setter
    private Localitate localitate;
    @Column(nullable = false)
    @Getter @Setter
    private String strada;
    @Column(nullable = false)
    @Getter @Setter
    private String numar;
    @Column(nullable = false)
    @Getter @Setter
    private String bloc;
    @Column(nullable = false)
    @Getter @Setter
    private String scara;
    // @ManyToMany(mappedBy = "adminAsociatii")
    @ManyToMany
    @JoinTable(
            name = "asociatie_admin",
            joinColumns = @JoinColumn(name = "asociatie_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    Set<User> admin =new HashSet<>();
    public Set<User> getAdmin() {
        return admin;
    }
    @Getter @Setter
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
    @JoinColumn(name = "asociatie_id", referencedColumnName = "id")
    List<Serviciu> serviciuList;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "asociatie_id", referencedColumnName = "id")
    @Getter @Setter
    List<Apartament> apartamente;
    public void setAdmin(Set<User> admin) {
        this.admin = admin;
    }
    public void addAdminToAsociatie(User user) {
        this.admin.add(user);
    }
    public void deleteAdminFromAsociatie(User user) {
        this.admin.remove(user);
    }

    public boolean findAdminFromAsociatie(User user) {
        return this.admin.contains(user);
    }

    public List<Apartament> getApartamente() {
        return this.apartamente;
    }
}
