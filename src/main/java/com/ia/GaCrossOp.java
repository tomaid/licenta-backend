package com.ia;

import java.util.Stack;

public class GaCrossOp {
    int[][] populatie;
    double rataIncrucisare;
    ListeProduse listeProduse;
    double pretMaxim;

    public GaCrossOp(int[][] populatie, double rataIncrucisare, ListeProduse listeProduse, double pretMaxim) {
        this.populatie = populatie;
        this.rataIncrucisare = rataIncrucisare;
        this.listeProduse = listeProduse;
        this.pretMaxim=pretMaxim;
    }

    public int[][] crossover(){
        Stack<Integer> selectedParents = new Stack<>(); // pun indecsii parintilor selectati intr-un stack pentru
        for (int i = 0; i < this.populatie.length; i++) { // a ii extrage mai tarziu
            double randomNr = Math.random();
            if(randomNr<this.rataIncrucisare){
                selectedParents.push(i);
            }
        }
        int i=0;
        while(i<selectedParents.size()){
            int[][] child1 = new int[1][this.populatie[0].length]; // creez copiii
            int[][] child2 = new int[1][this.populatie[0].length];
            int[][] aux = new int[1][this.populatie[0].length];
            int indexParinte1 = selectedParents.pop(); // extrag parintii din stiva
            int indexParinte2 = selectedParents.pop();
            int crossPoint = (int)(Math.floor((this.populatie[0].length-1)*Math.random())+1); // aleg aleatori punctele de crossover

            child1[0]=this.populatie[indexParinte1];
            for (int k = 0; k < child1[0].length; k++) {
                child1[0][k] = this.populatie[indexParinte1][k];
            }
            child2[0]=this.populatie[indexParinte2];
            for (int k = 0; k < child2[0].length; k++) {
                child2[0][k] = this.populatie[indexParinte2][k];
            }
            int k = 0;
            int end=crossPoint;
            while ( k < this.populatie[0].length) { // fac crossover
                if(k==end){
                    k=end+crossPoint;
                    end=k+crossPoint;
                }else{
                    aux[0][k] = child1[0][k];
                    child1[0][k] = child2[0][k]; // am folosit un auxiliar pentru swap
                    child2[0][k] = aux[0][k];
                    k++;
                }
            }
            double pretChild1 = this.listeProduse.getPretIndivid(child1); // verific daca valoarea totata a copilului este
            double pretChild2 = this.listeProduse.getPretIndivid(child2); // mai mica decat pretul maxim introdus de utilizator

            if(pretChild1<=pretMaxim) {// daca valoarea este mai mica decat pretul maxim copilul inlocuieste parintele
                for (int l = 0; l < child1[0].length; l++) {
                    this.populatie[indexParinte1][l] = child1[0][l];
                }
            }
            if(pretChild2<=pretMaxim){
                for (int l = 0; l < child2[0].length; l++) {
                    this.populatie[indexParinte2][l] = child2[0][l];
                }
            }
            i=i+2;
        }

        PrintPopulatie printPopulatie = new PrintPopulatie();
        printPopulatie.imprimarePopulatie("Populatie dupa crossover", this.populatie, this.listeProduse);
        return this.populatie;
    }

}
