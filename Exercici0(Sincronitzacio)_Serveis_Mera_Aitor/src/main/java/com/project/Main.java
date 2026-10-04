package com.project;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final int MIDA = 1_000_000;
    private static final double[] dades = new double[MIDA];

    private static final ConcurrentHashMap<String, Double> resultats = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        for (int i = 0; i < MIDA; i++) {
            dades[i] = i + 1;
        }

        CyclicBarrier barrier = new CyclicBarrier(3, () -> {
            System.out.println("Tots els càlculs han acabat. Resultats finals:");
            System.out.printf("Suma: %.2f%n", resultats.get("suma"));
            System.out.printf("Mitjana: %.2f%n", resultats.get("mitjana"));
            System.out.printf("Desviació estàndard: %.2f%n", resultats.get("desviacio"));
        });

        ExecutorService executor = Executors.newFixedThreadPool(3);

        Runnable tascaSuma = () -> {
            try {
                double suma = 0;
                for (double d : dades) {
                    suma += d;
                }
                resultats.put("suma", suma);
                System.out.println("Tasca suma completada.");
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                Thread.currentThread().interrupt();
            }
        };

        Runnable tascaMitjana = () -> {
            try {
                double suma = 0;
                for (double d : dades) {
                    suma += d;
                }
                resultats.put("mitjana", suma / dades.length);
                System.out.println("Tasca mitjana completada.");
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                Thread.currentThread().interrupt();
            }
        };

        Runnable tascaDesviacio = () -> {
            try {
                double suma = 0;
                for (double d : dades) {
                    suma += d;
                }
                double mitjana = suma / dades.length;
                double sumaQuadrats = 0;
                for (double d : dades) {
                    sumaQuadrats += (d - mitjana) * (d - mitjana);
                }
                resultats.put("desviacio", Math.sqrt(sumaQuadrats / dades.length));
                System.out.println("Tasca desviació estàndard completada.");
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                Thread.currentThread().interrupt();
            }
        };

        executor.submit(tascaSuma);
        executor.submit(tascaMitjana);
        executor.submit(tascaDesviacio);

        executor.shutdown();
        try {
            executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
