package com.ia;

public class CatProdus {
    int categorie, produs;

    public CatProdus(int categorie, int produs) {
        this.categorie = categorie;
        this.produs = produs;
    }

    public int getCategorie() {
        return categorie;
    }

    public void setCategorie(int categorie) {
        this.categorie = categorie;
    }

    public int getProdus() {
        return produs;
    }

    public void setProdus(int produs) {
        this.produs = produs;
    }
}
