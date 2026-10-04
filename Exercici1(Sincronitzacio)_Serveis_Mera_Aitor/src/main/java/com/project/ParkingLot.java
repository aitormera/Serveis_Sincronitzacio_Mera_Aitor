package com.project;

import java.util.concurrent.Semaphore;

public class ParkingLot {
    private final Semaphore places;

    public ParkingLot(int capacitat) {
        this.places = new Semaphore(capacitat);
    }

    public void entrar(String cotxe) throws InterruptedException {
        if (!places.tryAcquire()) {
            System.out.println(cotxe + " espera: l'aparcament està ple");
            places.acquire();
        }
        System.out.println(cotxe + " entra a l'aparcament");
    }

    public void sortir(String cotxe) {
        System.out.println(cotxe + " surt de l'aparcament");
        places.release();
    }
}
