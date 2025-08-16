/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.threads;

/**
 *
 * @author hcadavid
 */
public class CountThread extends Thread{
    public CountThread(int inicio, int fin) {
        for (int i = inicio; i <= fin; i++) {
            System.out.println(i);
        }
    }

    //Falta metodo run porque así como está no funciona bien
}