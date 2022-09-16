package com.licenta.licenta.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.licenta.licenta.dto.UserRegistrationDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="Users")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Long id;
    @Column(nullable = false, unique = true)
    @Getter @Setter
    private String user;
    @Column(nullable = false)
    @Getter @Setter
    private String pass;
    @Column(nullable = false)
    @Getter @Setter
    private String nume;
    @Column(nullable = false)
    @Getter @Setter
    private String prenume;
    @Column(nullable = false, unique = true)
    @Getter @Setter
    private String telefon;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    @Getter @Setter
    private Role role;


    // @ManyToMany(mappedBy = "admin")
    @ManyToMany
    @JoinTable(
            name = "asociatie_admin",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "asociatie_id"))
    Set<Asociatie> adminAsociatii = new HashSet<>();
    @OneToMany(fetch = FetchType.LAZY)
    @Getter @Setter
    List<Apartament> apartamente;


    public User(UserRegistrationDto userRegistrationDto){

        this.user = userRegistrationDto.getUser();
        this.pass = userRegistrationDto.getPass();
        this.nume = userRegistrationDto.getNume();
        this.prenume = userRegistrationDto.getPrenume();
        this.telefon = userRegistrationDto.getTelefon();
        this.role= userRegistrationDto.getRole();
    }

    public Set<Asociatie> getAdminAsociatii() {
        return adminAsociatii;
    }
    public void setAdminAsociatii(Set<Asociatie> adminAsociatii) {
        this.adminAsociatii = adminAsociatii;
    }
    public void addAdminToAsociatie(Asociatie asociatie) {
        this.adminAsociatii.add(asociatie);
    }
    public void deleteAdminFromAsociatie(Asociatie asociatie) {
        this.adminAsociatii.remove(asociatie);
    }
    public boolean findAdminFromAsociatie(Asociatie asociatie) {
        return this.adminAsociatii.contains(asociatie);
    }

}
