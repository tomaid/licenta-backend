package com.ia;

import com.licenta.licenta.dto.AlgoGeneticProduseDto;
import com.licenta.licenta.dto.AlgoGeneticReturnareLista;
import com.licenta.licenta.dto.AlgoGeneticReturnareListe;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class GA {
    int marimePopulatie;
    double rataMutatie, rataIncrucisare, pret;
    List<AlgoGeneticProduseDto> produseDtoList;
    private static final DecimalFormat df = new DecimalFormat("0.00");
    public GA(){}

    public List<AlgoGeneticReturnareListe> runAlgoritm(){
        ListeProduse listeProduse = new ListeProduse(this.produseDtoList, this.pret);
        listeProduse.ComandaArray();
        List produse = listeProduse.getProduseComanda();
        double pretMaxim = listeProduse.getPretMaxim();
        RandCrom randCrom = new RandCrom(produse, this.marimePopulatie);
        int[][] populatie = randCrom.generareCromozom();
        int[][] rezultatFinal;
        int n = 1; // integer pentru numarul generatiilor
        while(true) { // initiem loop-ul
            GAFitness fitness = new GAFitness(populatie, listeProduse, pretMaxim); // functia de fitness
            fitness.calculFitnessIndivid(); // calculeaza fitness individ
            fitness.calculConsumPopulatie(); // calculeaza consum total per populatie
            // if(fitness.verificareConditii()){ // aceasta este conditia de incetare
            if((n==5000)||fitness.verificareConditii()){ // aceasta este conditia de incetare
                rezultatFinal = fitness.rezultatFinal();
                break;
            }
            GaSelectRoulette selCrom = new GaSelectRoulette(populatie, listeProduse, fitness, pretMaxim);
            GaCrossOp crossover = new GaCrossOp(selCrom.probabilitateSelectie(), this.rataIncrucisare, listeProduse, pretMaxim);
            GAMutation mutation = new GAMutation(crossover.crossover(), rataMutatie, listeProduse, pretMaxim);
            populatie = mutation.mutatie();
            n++;
        }
        List<AlgoGeneticReturnareListe> algoGeneticReturnareListeList = new ArrayList<>();

        AlgoGeneticReturnareListe listaEconomica = new AlgoGeneticReturnareListe();
        listaEconomica.setGeneratii(n);
        listaEconomica.setVarianta("Varianta economică din punct de vedere al consumului");
//        for (int i = 0; i < rezultatFinal.length ; i++) {
//            for (int j = 0; j < rezultatFinal[i].length; j++) {
//                System.out.println(rezultatFinal[i][j]);
//            }
//        }
        listaEconomica.setPretTotal(rezultatFinal[0][rezultatFinal[0].length-2]);
        double a = rezultatFinal[0][rezultatFinal[0].length-1];
        listaEconomica.setTotalKw(a);
        List<AlgoGeneticReturnareLista> algoGeneticReturnareListaList = new ArrayList<>();
        for (int k = 0; k < rezultatFinal[0].length-2; k++) {
            AlgoGeneticReturnareLista algoGeneticReturnareLista = new AlgoGeneticReturnareLista();
            algoGeneticReturnareLista.setPret(listeProduse.getProdus(rezultatFinal[0][k]).getPret());
            algoGeneticReturnareLista.setDenumire(listeProduse.getProdus(rezultatFinal[0][k]).getInfo());
            algoGeneticReturnareLista.setConsum(((double)listeProduse.getProdus(rezultatFinal[0][k]).getConsum()));
            algoGeneticReturnareListaList.add(algoGeneticReturnareLista);
        }
        listaEconomica.setProdusePrimite(algoGeneticReturnareListaList);
        algoGeneticReturnareListeList.add(listaEconomica);

      //  ===
        AlgoGeneticReturnareListe listaIeftina = new AlgoGeneticReturnareListe();
        listaIeftina.setGeneratii(n);
        listaIeftina.setVarianta("Varianta ieftină din punct de vedere al prețului");
        listaIeftina.setPretTotal(rezultatFinal[1][rezultatFinal[1].length-2]);
        double b = rezultatFinal[1][rezultatFinal[1].length-1];
        listaIeftina.setTotalKw(b);
        List<AlgoGeneticReturnareLista> algoGeneticReturnareListaListIeftina = new ArrayList<>();
        for (int k = 0; k < rezultatFinal[0].length-2; k++) {
            AlgoGeneticReturnareLista algoGeneticReturnareLista = new AlgoGeneticReturnareLista();
            algoGeneticReturnareLista.setPret(listeProduse.getProdus(rezultatFinal[1][k]).getPret());
            algoGeneticReturnareLista.setDenumire(listeProduse.getProdus(rezultatFinal[1][k]).getInfo());
            algoGeneticReturnareLista.setConsum(((double)listeProduse.getProdus(rezultatFinal[1][k]).getConsum()));
            algoGeneticReturnareListaListIeftina.add(algoGeneticReturnareLista);
        }
        listaIeftina.setProdusePrimite(algoGeneticReturnareListaListIeftina);
        algoGeneticReturnareListeList.add(listaIeftina);

        return algoGeneticReturnareListeList;
    }


    public void setMarimePopulatie(int marimePopulatie) {
        this.marimePopulatie = marimePopulatie;
    }

    public void setRataMutatie(double rataMutatie) {
        this.rataMutatie = rataMutatie;
    }

    public void setRataIncrucisare(double rataIncrucisare) {
        this.rataIncrucisare = rataIncrucisare;
    }

    public void setPret(double pret) {
        this.pret = pret;
    }

    public void setProduseDtoList(List<AlgoGeneticProduseDto> produseDtoList) {
        this.produseDtoList = produseDtoList;
    }
}
