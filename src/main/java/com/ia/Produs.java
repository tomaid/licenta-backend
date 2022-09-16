package com.ia;

public class Produs {
    int id, consum;
    double pret;
    String info;

    public Produs( int id, int consum, double pret) {
        this.id = id;
        this.consum = consum;
        this.pret = pret;
    }
    public Produs( int id, int consum, double pret, String info) {
        this.id = id;
        this.consum = consum;
        this.pret = pret;
        this.info = info;
    }

    public int getId() {
        return id;
    }
    public int getConsum() {
        return consum;
    }
    public double getPret() {
        return pret;
    }
    public String getInfo() {
        return this.info;
    }
}
