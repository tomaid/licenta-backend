package com.ia;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GAFitness {
    int[][] populatie;
    ListeProduse listeProduse;
    double[] pret;
    double pretPopulatie;
    float[] consum;
    float consumPopulatie;
    double pretMaxim;
    Map<Float, Integer> mapProcentConsum;
    int[] consumIndivid;

    public GAFitness(int[][] populatie, ListeProduse listeProduse, double pretMaxim) {
        this.mapProcentConsum = new HashMap<>();
        this.populatie = populatie;
        this.listeProduse = listeProduse;
        this.pret = new double[this.populatie.length];
        this.consum = new float[this.populatie.length];
        this.consumIndivid = new int[this.populatie.length];
        this.pretMaxim= pretMaxim;
    }
    public void calculFitnessIndivid(){ // calculam fitness (pret si consum) pentru fiecare cromozom

        for (int i = 0; i < this.populatie.length; i++) {
            double pret = 0;
            int consum = 0;
            for (int j = 0; j < this.populatie[i].length; j++) {
                pret = pret + this.listeProduse.getProdusComanda(j, this.populatie[i][j]).getPret();
                consum = consum + this.listeProduse.getProdusComanda(j, this.populatie[i][j]).getConsum();
            }
            this.pret[i] = pret;
            this.consumIndivid[i]=consum;
            float procentConsum = 1/(float)consum; // problema de minimizare a consumului
            float roundedProcentConsum = round(procentConsum);
            this.consum[i] = procentConsum; // problema de minimizare a consumului
            if(this.mapProcentConsum.containsKey(roundedProcentConsum)){
                this.mapProcentConsum.put(roundedProcentConsum, this.mapProcentConsum.get(roundedProcentConsum)+1);
            }
            else{
                this.mapProcentConsum.put(roundedProcentConsum, 1);
            }
        }
    }

    public void calculConsumPopulatie(){ // aflam consum total populatie
        double pret=0;
        float consum = 0;
        for (int i = 0; i < populatie.length; i++) {
            consum=this.consum[i]+consum;
            pret = pret + this.pret[i];
        }
        this.pretPopulatie=pret;
        this.consumPopulatie=consum;
    }

    private float round(float d) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(6, RoundingMode.HALF_UP);
        return bd.floatValue();
    }

    public float[] getConsumIndivid() {
        return consum;
    }

    public float getConsumPopulatie() {
        return consumPopulatie;
    }
    public double[] getPret() {
        return pret;
    }

    public boolean verificareConditii(){
        for (Float i:this.mapProcentConsum.keySet()
        ) {
            if((this.mapProcentConsum.get(i)>=this.populatie.length*0.97) && ((this.pretPopulatie/this.populatie.length)<(this.pretMaxim*1.2)))
            {
                return true;
            }
        }
        return false;
    }

    public int[][] rezultatFinal(){
        int[][] variante = new int[2][this.populatie[0].length+2];
        int[][] populatieFinala = new int[this.populatie.length][this.populatie[0].length+2];
        for (int j = 0; j < this.populatie.length; j++) {
            for (int k = 0; k < this.populatie[j].length; k++) {
                populatieFinala[j][k] = this.listeProduse.getProdusComanda(k, this.populatie[j][k]).getId();
            }
            populatieFinala[j][this.populatie[0].length] = (int)Math.round(this.pret[j]);
            populatieFinala[j][this.populatie[0].length+1] =this.consumIndivid[j];
        }

        Arrays.sort(populatieFinala, (b,a) -> b[b.length-1] - a[a.length-1]);
        variante[0] = populatieFinala[0];
        Arrays.sort(populatieFinala, (b,a) -> b[b.length-2] - a[a.length-2]);
        variante[1] = populatieFinala[0];
        return variante;
    }

}
