/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;

import java.util.List;

/**
 *
 * @author hcadavid
 */
public class Main {
    
    public static void main(String a[]){
        HostBlackListsValidator hblv = new HostBlackListsValidator();
        int nThreads = 100;
        String ip = "202.24.34.55";
        if (a.length > 0) {
            ip = a[0];
        }
        if (a.length > 1) {
            try {
                nThreads = Integer.parseInt(a[1]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid thread count, using default: " + nThreads);
            }
        }
        int cores = Runtime.getRuntime().availableProcessors();
        System.out.println("Número de núcleos disponibles: " + cores);
        long start = System.currentTimeMillis();
        List<Integer> blackListOcurrences = hblv.checkHost(ip, nThreads);
        long end = System.currentTimeMillis();
        System.out.println("The host was found in the following blacklists: " + blackListOcurrences);
        System.out.println("Tiempo de ejecución: " + (end - start) + " ms");
    }
    
}