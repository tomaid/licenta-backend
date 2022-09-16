package com.ia;

import java.util.List;
import java.util.Random;

public class GAMutation {
    int[][] populatie;
    double rataMutatie;
    ListeProduse listeProduse;
    double pretMaxim;
    public GAMutation(int[][] populatie, double rataMutatie, ListeProduse listeProduse, double pretMaxim) {
        this.populatie = populatie;
        this.rataMutatie = rataMutatie;
        this.listeProduse = listeProduse;
        this.pretMaxim = pretMaxim;
    }

    public int[][] mutatie(){
        int lungimeCromozom = populatie[0].length;
        double totalGen = (double)(populatie.length*lungimeCromozom)/2;
        int numarMutatii = (int)Math.ceil((float)rataMutatie*totalGen);
        while (numarMutatii>0){
            int[][] cromozomModificat = new int[1][this.populatie[0].length]; // se itiniaza cromozomul
            int randomNumber = (int)(Math.round((totalGen-1)*Math.random())+1);
            int i = (int) Math.ceil((double)randomNumber*2/lungimeCromozom)-1; // se alege  individ
            int j = ((randomNumber*2)%lungimeCromozom); // se alege o gena
            List listaListeProduse = listeProduse.getProduseComanda(); // caut numarul listei cu produse in lista cu mai multe liste...
            List listaProduse = (List)listaListeProduse.get(j); // vad cate produse are lista respectiva pentru a afla upperBound
            Random rand = new Random();
            int indexProdus = rand.nextInt(listaProduse.size()); // generez un integer cu interval intre 0 si marimea listei
            for (int k = 0; k < cromozomModificat[0].length; k++) { // copiez cromozomul in cromozomul care va muta
                cromozomModificat[0][k] = this.populatie[i][k];
            }
            cromozomModificat[0][j] = indexProdus; // se creeaza mutatia
            double pretCromozomModificat = this.listeProduse.getPretIndivid(cromozomModificat);
            if(pretCromozomModificat<=pretMaxim) {  // verific daca valoarea totata a copilului este mai mica
                this.populatie[i][j] = indexProdus;
            }
            numarMutatii--;
        }
        PrintPopulatie printPopulatie = new PrintPopulatie();
        printPopulatie.imprimarePopulatie("Populatie dupa mutatie", this.populatie, this.listeProduse);
        return this.populatie;
    }
}
