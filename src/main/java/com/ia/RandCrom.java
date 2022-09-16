package com.ia;
import java.util.*;

public class RandCrom {
    List produse;
    int marimePopulatie;
    public RandCrom(List produse, int marimePopulatie) {
        this.produse = produse;
        this.marimePopulatie = marimePopulatie;
    }
    public int[][] generareCromozom(){  // generare cromozom random.
        int marime = this.produse.size();
        int[][] populatie = new int[this.marimePopulatie][marime];
        for (int i = 0; i < this.marimePopulatie; i++) { // facem loop prin lista care contine listele cu produse
            for (int j = 0; j < marime; j++) {
                List<Produs> lista = (List) this.produse.get(j);
                Random rand = new Random();
                int indexProdus = rand.nextInt(lista.size());
                populatie[i][j]= indexProdus;
            }
        }
        return populatie;
    }
}
