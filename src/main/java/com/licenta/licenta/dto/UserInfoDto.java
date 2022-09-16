package com.licenta.licenta.dto;
import com.licenta.licenta.model.Role;
public class UserInfoDto {
    private Long id;
    private String user;
    private String nume;
    private String prenume;
    private String telefon;
    private Role role;

    public UserInfoDto() {
    }

    public UserInfoDto(Long id, String user, String nume, String prenume, String telefon, Role role) {
        this.id = id;
        this.user = user;
        this.nume = nume;
        this.prenume = prenume;
        this.telefon = telefon;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
