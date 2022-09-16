package com.ia;

public class Comanda {
    int cicluri_pe_luna,durata_ciclu_in_minute;

    public Comanda(int cicluri_pe_luna, int durata_ciclu_in_minute) {
        this.cicluri_pe_luna = cicluri_pe_luna;
        this.durata_ciclu_in_minute = durata_ciclu_in_minute;
    }

    public int getCicluri_pe_luna() {
        return cicluri_pe_luna;
    }

    public int getDurata_ciclu_in_minute() {
        return durata_ciclu_in_minute;
    }
}
