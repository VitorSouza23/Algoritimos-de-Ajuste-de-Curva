/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsc.ajustedecurva;

import java.math.BigDecimal;

/**
 *
 * @author Vitor
 */
public class AjusteLinear {
    private static BigDecimal vetXi[];
    private static BigDecimal vetYi[];
    private static BigDecimal vetd[];
    private static BigDecimal B0, B1;
    
    private static void criarVetoresBigDecimal(){
        double vetx[] = {1.3, 3.4, 5.1, 6.8, 8.0};
        double vety[] = {2.0, 5.2, 3.8, 6.1, 5.8};
        
        
        vetXi = new BigDecimal[vetx.length];
        vetYi = new BigDecimal[vety.length];
        vetd = new BigDecimal[vetx.length];
        
        for(int x = 0; x < vetXi.length; x++){
            vetXi[x] = new BigDecimal(Double.toString(vetx[x]));
            vetYi[x] = new BigDecimal(Double.toString(vety[x]));
        }
    }
    
    public static void calcularD(){
        criarVetoresBigDecimal();
        B0 = new BigDecimal("0");
        B1 = new BigDecimal("1");
        BigDecimal D = BigDecimal.ZERO;
        System.out.println("Vetor d: yi - B0 - B1xi");
        for(int x = 0; x < vetd.length; x++){
            System.out.print("d[" + x + "]: " + vetYi[x] + " - " + B0 + " - " + B1 + " * " + vetXi[x]);
            vetd[x] = vetYi[x].subtract(B0.add(B1.multiply(vetXi[x])));
            System.out.println(" = " + vetd[x]);
        }
        for(int x = 0; x < vetd.length; x++){
            D = D.add(vetd[x].pow(2));
        }
        
        System.out.println("D = " + D);
    }
    
    
}
