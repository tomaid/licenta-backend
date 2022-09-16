package com.licenta.licenta.dto;

import com.licenta.licenta.model.Role;
import com.licenta.licenta.model.User;

public class UserRegistrationDto {
    private String user;
    private String pass;
    private String nume;
    private String prenume;
    private String telefon;
    private Role role;

    public UserRegistrationDto(){}
    public UserRegistrationDto(User user){
        this.user=user.getUser();
        this.pass=user.getPass();
        this.nume=user.getNume();
        this.prenume=user.getPrenume();
        this.telefon=user.getTelefon();
        this.role=user.getRole();
    }



    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
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
