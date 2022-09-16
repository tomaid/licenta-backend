package com.ia;

import com.licenta.licenta.dto.AlgoGeneticProduseDto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListeProduse {
    double pretMaxim;
    List produseComanda;
    Map<Integer,Produs> totalProduse = new HashMap();
    List<AlgoGeneticProduseDto> produseDtoList;

    public ListeProduse(List<AlgoGeneticProduseDto> produseDtoList, double pretMaxim) {
        this.produseDtoList = produseDtoList;
        this.pretMaxim = pretMaxim;
    }
    private List ProduseArray(int cat, int cicluriLuna, int durataCiclu) { // o metoda care pune produsele in lista
        List<Produs> produse = new ArrayList<>();
        Connection conn;
        String query = "SELECT id, model_dispozitiv, categorie_id, consum, pret FROM dispozitive WHERE categorie_id = " + cat;
        try {
           // Class.forName("com.mysql.cj.jdbc.Driver");
            // System.out.println("Driver Loaded");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/licenta","spring","P@rola");
            //Database Name - testDB, Username - "root", Password - ""
           // System.out.println("Connected...");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while(rs.next()){
                // categorie, id, consum, pret -> consumul este in watt - minut
                int categorie = rs.getInt("categorie_id");
                int consum = rs.getInt("consum");
                double pret = rs.getDouble("pret");
                int idProdus = rs.getInt("id");
                String info = rs.getString("model_dispozitiv");
                Produs produs = new Produs(
                        idProdus,
                        consum * cicluriLuna * durataCiclu,
                        pret);
                produse.add(produs);
                this.totalProduse.put(idProdus,new Produs(idProdus, consum * cicluriLuna * durataCiclu, pret, info));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return produse;
    }

    public void ComandaArray(){  // o lista care contine alte liste in functie de categoria din care face parte fiecare produs cautat
        List<List> comanda = new ArrayList<>();
            for (AlgoGeneticProduseDto produs: produseDtoList
                 )
            {
                    // categorie, cicluri_pe_luna, durata_ciclu_in_minute
                    int cat = produs.getIdCategorie();
                    int cicluriLuna=produs.getCicluNumere();
                    int durataCiclu= produs.getDurataCiclu();;
                    List produse = this.ProduseArray(cat, cicluriLuna, durataCiclu);
                    comanda.add(produse);
                }
        this.produseComanda = comanda;
    }

    public double getPretMaxim() {
        return pretMaxim;
    }

    public Produs getProdusComanda(int i,int j) { // getter pentru a returna produsul dintr-o lista
        List lista = (List) this.produseComanda.get(i);
        return (Produs)lista.get(j);
    }
    public int getConsumIndivid(int[][] populatie) {
        int consum = 0;
        for (int j = 0; j < populatie[0].length; j++) {
            consum = consum + this.getProdusComanda(j, populatie[0][j]).getConsum();
        }
        return consum;
    }
    public double getPretIndivid(int[][] populatie) {
        double pret = 0;
        for (int j = 0; j < populatie[0].length; j++) {
            pret = pret + this.getProdusComanda(j, populatie[0][j]).getPret();
        }
        return pret;
    }

    public List getProduseComanda() {
        return this.produseComanda;
    }

    public Produs getProdus(int id) {
        return totalProduse.get(id);
    }
}
