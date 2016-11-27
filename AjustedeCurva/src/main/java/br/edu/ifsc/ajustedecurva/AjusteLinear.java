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
        double vetx[] = {183,173,168,188,158,163,193,163,178};
        double vety[] = {79,69,70,81,61,63,79,71,73};
        
        
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
        System.out.println("Ajuste de Curva: " + B0 + " + " + B1 + " * x");
        System.out.println("\n");
        
        System.out.println("===============================================");
        System.out.println("Coeficiente de Determinação:");
        BigDecimal coeficiente = BigDecimal.ZERO;
        BigDecimal somaYi2;
        
        somaYi2 = BigDecimal.ZERO;
        System.out.println("Somatório (Yi)^2:");
        for (int x = 0; x < vetYi.length; x++) {
            somaYi2 = somaYi2.add((vetYi[x].pow(2)).setScale(CASA_DECIMAL, ROUND_MODE));
            System.out.println("(y[" + x + "])^2 = " + (vetYi[x].pow(2).setScale(CASA_DECIMAL, ROUND_MODE)));
        }
        System.out.println("Somatório (Yi) ^ 2 = " + somaYi2);
        System.out.println("===============================================");
        System.out.println("\n");
        
        coeficiente = (somaXiYi.subtract(somaXi.multiply(somaYi.divide(N, CASA_DECIMAL_DIVISOR, ROUND_MODE)))).pow(2).
                divide((somaXi2.subtract(somaXi.pow(2).divide(N, CASA_DECIMAL_DIVISOR, ROUND_MODE))).
                        multiply(somaYi2.subtract(somaYi.pow(2).divide(N, CASA_DECIMAL_DIVISOR, ROUND_MODE))), CASA_DECIMAL_DIVISOR, ROUND_MODE);
        System.out.println("\n");
        System.out.println("Coeficiente de Determinação = R^2");
        System.out.println("R^2 = [somaXi - somaXi*(somaYi / n)] ^ 2 / [soma(Xi^2) - (somaXi)^2 / n] * [soma(Yi^2) - (somaYi)^2 / n]");
        System.out.println("\n");
        System.out.println("R^2 =");
        System.out.println("[" + somaXiYi + " - " + somaXi + " * (" + somaYi +" / " + N +")]^2");
        System.out.println("________________________________");
        System.out.println("[" + somaXi2 +" - ((" + somaXi +")^2 / " + N + ")] * [" + somaYi2 +" - ((" + somaYi +")^2 / " + N + ")]");
        System.out.println("\n");
        System.out.println("R^2 = " + coeficiente);
        System.out.println("===============================================");
    }
    
    public static void calcularYAtravesDeAjuste(double valor){
        BigDecimal bigValor = new BigDecimal(Double.toString(valor));
        System.out.println("\n");
        System.out.println("==============================================");
        System.out.println("Calculando Y através do Ajuste de Curva:");
        System.out.println("y = " + B0 + " + " + B1 + " * " + bigValor);
        BigDecimal resultado = B0.add(B1.multiply(bigValor)).setScale(CASA_DECIMAL, ROUND_MODE);
        System.out.println("y = " + resultado);
        System.out.println("==============================================");
    }
    
    
}
