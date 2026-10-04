package com.project;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final int CAPACITAT = 3;
    private static final int NUM_COTXES = 6;

    public static void main(String[] args) {
        ParkingLot parking = new ParkingLot(CAPACITAT);
        ExecutorService executor = Executors.newFixedThreadPool(NUM_COTXES);

        for (int i = 1; i <= NUM_COTXES; i++) {
            executor.submit(cotxe(i, parking));
        }

        executor.shutdown();
        try {
            executor.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static Runnable cotxe(int id, ParkingLot parking) {
        return () -> {
            String nom = "Cotxe " + id;
            try {
                parking.entrar(nom);
                try {
                    Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 2001));
                } finally {
                    parking.sortir(nom);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };
    }
}
