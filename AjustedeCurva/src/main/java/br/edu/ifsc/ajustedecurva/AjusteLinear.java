/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsc.ajustedecurva;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author Vitor
 */
public class AjusteLinear {
    private static BigDecimal vetXi[];
    private static BigDecimal vetYi[];
    private static BigDecimal vetd[];
    private static BigDecimal B0, B1;
    
    private static final RoundingMode ROUND_MODE = RoundingMode.HALF_EVEN;
    private static final int CASA_DECIMAL = 3;
    private static final int CASA_DECIMAL_DIVISOR = 15;
    
    private static void criarVetoresBigDecimal(){
        double vetx[] = {1872, 1890, 1900, 1920, 1940, 1950, 1960,1970, 1980,1991};
        double vety[] = {9.9, 14.3, 17.4, 30.6, 41.2, 51.9,70.2,93.1,119.0, 146.2};
        
        
        vetXi = new BigDecimal[vetx.length];
        vetYi = new BigDecimal[vety.length];
        vetd = new BigDecimal[vetx.length];
        
        for(int x = 0; x < vetXi.length; x++){
            vetXi[x] = new BigDecimal(Double.toString(vetx[x])).setScale(CASA_DECIMAL, ROUND_MODE);
            vetYi[x] = new BigDecimal(Double.toString(vety[x])).setScale(CASA_DECIMAL, ROUND_MODE);
        }
    }
    
    public static void calcularD(){
        criarVetoresBigDecimal();
        calcularB0B1();
        BigDecimal D = BigDecimal.ZERO;
        System.out.println("Vetor d: yi - B0 - B1xi");
        System.out.println("Vetor d: yi - " + B0 + " - " + B1 + " * xi");
        for(int x = 0; x < vetd.length; x++){
            System.out.print("d[" + x + "]: " + vetYi[x] + " - " + B0 + " - " + B1 + " * " + vetXi[x]);
            vetd[x] = vetYi[x].subtract(B0.add(B1.multiply(vetXi[x]))).setScale(CASA_DECIMAL, ROUND_MODE);
            System.out.println(" = " + vetd[x]);
        }
        for(int x = 0; x < vetd.length; x++){
            D = D.add(vetd[x].pow(2)).setScale(CASA_DECIMAL, ROUND_MODE);
        }
        
        System.out.println("D = " + D);
    }
    
    private static void calcularB0B1(){
        int n = vetXi.length;
        BigDecimal somaXiYi = BigDecimal.ZERO;
        BigDecimal somaXi = BigDecimal.ZERO;
        BigDecimal somaXi2 = BigDecimal.ZERO;
        BigDecimal somaYi = BigDecimal.ZERO;
        
        for(int x = 0; x < n; x++){
            somaXiYi = somaXiYi.add(vetXi[x].multiply(vetYi[x])).setScale(CASA_DECIMAL, ROUND_MODE);
        }
    
        for(int x = 0; x < n; x++){
            somaXi = somaXi.add(vetXi[x]).setScale(CASA_DECIMAL, ROUND_MODE);
        }
        
        for(int x = 0; x < n; x++){
            somaYi = somaYi.add(vetYi[x]).setScale(CASA_DECIMAL, ROUND_MODE);
        }
        
        for(int x = 0; x < n; x++){
            somaXi2 = somaXi2.add(vetXi[x].pow(2)).setScale(CASA_DECIMAL, ROUND_MODE);
        }
        
        System.out.println("n: " + n);
        System.out.println("Somatório Xi: " + somaXi);
        System.out.println("Somatório Yi: " + somaYi);
        System.out.println("Somatório XiYi: " + somaXiYi);
        System.out.println("Somatório Xi^2: " + somaXi2);
        
        
        BigDecimal N = new BigDecimal(n).setScale(CASA_DECIMAL, ROUND_MODE);
        
        BigDecimal nXsomaXiYi = N.multiply(somaXiYi).setScale(CASA_DECIMAL, ROUND_MODE);
        BigDecimal somaXiXsomaYi = somaXi.multiply(somaYi).setScale(CASA_DECIMAL, ROUND_MODE);
        BigDecimal nXsomaXi2 = N.multiply(somaXi2).setScale(CASA_DECIMAL, ROUND_MODE);
        
        System.out.println("\n");
        System.out.println("n * Somatório XiYi: " + nXsomaXiYi);
        System.out.println("Somatório Xi * Somatório Yi: " + somaXiXsomaYi);
        System.out.println("n * Somatório Xi^2: " + nXsomaXi2);
        System.out.println("\n");
        
        B1 = nXsomaXiYi.subtract(somaXiXsomaYi).divide(nXsomaXi2.subtract(somaXi.pow(2)), CASA_DECIMAL_DIVISOR, ROUND_MODE);
        B0 = somaYi.subtract(somaXi.multiply(B1)).divide(N, CASA_DECIMAL_DIVISOR, ROUND_MODE);
    
        System.out.println("B0: " + B0);
        System.out.println("B1: " + B1);
        System.out.println("\n");
        
    }
}
