package com.ia;
public class GaSelectRoulette {
    int[][] populatie;
    ListeProduse listeProduse;
    GAFitness fitness;
    double pretMaxim;
    public GaSelectRoulette(int[][] populatie, ListeProduse listeProduse, GAFitness fitness, double pretMaxim) {
        this.populatie = populatie;
        this.listeProduse = listeProduse;
        this.fitness = fitness;
        this.pretMaxim = pretMaxim;
    }

    public int[][] probabilitateSelectie(){
        float[] P = new float[this.populatie.length];
        float[] C = new float[this.populatie.length];
        int[][] result = new int[this.populatie.length][this.populatie[0].length];
        for (int i = 0; i < this.populatie.length; i++) { // probabilitate P[i]
            P[i]=1-(this.fitness.getConsumIndivid()[i]/ this.fitness.getConsumPopulatie());
        }
        for (int i = 0; i < populatie.length; i++) { // probabilitate cumulativa
            for (int j = 0; j < i; j++) {
                C[i]=C[i]+P[j];
            }
        }

        for (int i = 0; i < populatie.length; i++) { // se foloseste metoda ruletei.
            double randomNr = Math.random();

            for (int j = 0; j < populatie.length; j++) {
                if(C[j]>=randomNr){
                    if(fitness.getPret()[j]<=pretMaxim){ //
                        for (int k = 0; k < result[i].length; k++) {
                            result[i][k] = this.populatie[j][k];
                        }
                        break;
                    }
                    else{
                        for (int k = 0; k < result[i].length; k++) {
                            result[i][k] = this.populatie[i][k];
                        }
                    }
                }
            }
        }
        PrintPopulatie printPopulatie = new PrintPopulatie();
        printPopulatie.imprimarePopulatie("Populatie dupa ruleta", result, this.listeProduse);
        return result; // returneaza populatia
    }
}
