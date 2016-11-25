/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsc.sistemaslineares.exatos;

import java.math.BigDecimal;
import java.math.RoundingMode;



/**
 *
 * @author Vitor
 */
public class FatoracaoCholesky {
     private static final double matrizDouble[][] = {
         {8.0, -1.0, 0.5, 2.1, -1.2, 17.461},
        {-1.0, 9.0, -1.2, 3.1, 0.5, 10.025},
        {0.5, 1.2, 6.0, -0.4, -1.5, -17.386},
        {2.1, -3.1, -0.4, 8.0, 1.0, 1.103},
        {-1.2, 0.5, 1.5, 1.0, 6.0, 0.285}
        
    };
     
    
    private static BigDecimal matriz[][];
    private static BigDecimal vetb[];
    private static BigDecimal vetx[];
    private static BigDecimal matG[][];
    private static BigDecimal matGt[][];
    private static BigDecimal vety[];
    private static final RoundingMode ROUND_MODE = RoundingMode.HALF_EVEN;
    private static final int CASA_DECIMAL = 3;
    private static final int CASA_DECIMAL_DIVISOR = 15;
    
    private static void criarMatrizBigDecimal(){
        matriz = new BigDecimal[matrizDouble.length][matrizDouble[0].length];
        for(int l = 0; l < matrizDouble.length; l++){
            for(int c = 0; c < matrizDouble[0].length; c++){
                matriz[l][c] = new BigDecimal(matrizDouble[l][c]).setScale(CASA_DECIMAL, ROUND_MODE);
            }
        }
    }
   

    public static void calcularVetorX() {
       criarMatrizBigDecimal();
        matG = new BigDecimal[matriz.length][matriz[0].length - 1];
        for (int l = 0; l < matriz.length; l++) {
            for (int c = 0; c < matriz[0].length - 1; c++) {
                if(l == c){
                    BigDecimal resultado = BigDecimal.ZERO;
                    for(int i = 0; i < c; i++){
                        resultado = resultado.add(matG[l][i].pow(2).setScale(CASA_DECIMAL, ROUND_MODE)).setScale(CASA_DECIMAL, ROUND_MODE);
                    }
                    matG[l][c] = matriz[l][c].subtract(resultado).pow(1/2).setScale(CASA_DECIMAL, ROUND_MODE);
                    
                }else if(l > c){
                    BigDecimal resultado = BigDecimal.ZERO;
                    for(int i = 0; i < c ; i++){
                        resultado = resultado.add(matG[l][i].multiply(matG[c][i].setScale(CASA_DECIMAL, ROUND_MODE))).setScale(CASA_DECIMAL, ROUND_MODE);
                    }
                    matG[l][c] = matriz[l][c].subtract(resultado).setScale(CASA_DECIMAL, ROUND_MODE).divide(matG[c][c], CASA_DECIMAL_DIVISOR, ROUND_MODE);
                    
                }else{
                    matG[l][c] = BigDecimal.ZERO;
                }
            } 
        }
        matGt = new BigDecimal[matriz.length][matriz[0].length - 1];
        for(int l = 0; l < matG.length; l++){
            for(int c = 0; c < matG[0].length; c++){
                matGt[c][l] = matG[l][c]; 
            }
        }
        vety = new BigDecimal[matriz.length];
        mostrarMatriz(matG, "G");
        System.out.println("\n");
        mostrarMatriz(matGt, "G Transposta");
        System.out.println("\n");
        
        
        mostrarVetorX();
    }

    

    private static void mostrarMatriz(BigDecimal mat[][], String nome) {
        System.out.println("Matriz " + nome + ":");
        for (int x = 0; x < mat.length; x++) {
            for (int y = 0; y < mat[0].length; y++) {
                System.out.print(" " + mat[x][y] + " ");
            }
            System.out.println("");
        }
    }

    private static void mostrarVetorX() {
        
        vetx = new BigDecimal[matriz.length];
        vetb = new BigDecimal[matriz.length];
        
        
        for(int b = 0; b < vetb.length; b++){
            vetb[b] = matriz[b][matriz[0].length -1];
        }
        
        mostrarVetorB();
        calcularGyb();
        mostrarVetY();
        calcularGtxy();
        
        System.out.println("\n");
        System.out.println("Vetor X:");
        for(int x = 0; x < vetx.length; x++){
            System.out.println(vetx[x]);
        }
    }
    
    private static void mostrarVetorB(){
        System.out.println("\n");
        System.out.println("Vetor B:");
        for(int b = 0; b < vetb.length; b++){
            System.out.println(vetb[b]);
        }
    }
    
    
    
  
    
    private static void calcularGyb(){
        BigDecimal res = BigDecimal.ZERO;
        for(int l = 0; l < matG.length; l++){
            for(int c = 0; c < matG[0].length; c++){
                if(l == c){
                    break;
                }else{
                    res = res.add(matG[l][c].multiply(vety[c]).setScale(CASA_DECIMAL, ROUND_MODE)).setScale(CASA_DECIMAL, ROUND_MODE);
                }   
            }
            vety[l] = vetb[l].subtract(res).setScale(CASA_DECIMAL, ROUND_MODE).divide(matG[l][l], CASA_DECIMAL_DIVISOR, ROUND_MODE);
            res = BigDecimal.ZERO;
        }
    }
    
    private static void mostrarVetY(){
        System.out.println("\n");
        System.out.println("Vetor Y:");
        for(int l = 0; l < vety.length; l++){
                System.out.println(" " + vety[l] + " ");
        }
    }
    
    private static void calcularGtxy(){
        BigDecimal res = BigDecimal.ZERO;
        for(int l = matGt.length - 1; l >= 0; l--){
            for(int c = matGt[0].length - 1; c >= 0; c--){
                if(l == c){
                    break;
                }else{
                    res = res.add(matGt[l][c].multiply(vetx[c]).setScale(CASA_DECIMAL, ROUND_MODE)).setScale(CASA_DECIMAL, ROUND_MODE);
                }   
            }
            vetx[l] = vety[l].subtract(res).setScale(CASA_DECIMAL, ROUND_MODE).divide(matGt[l][l], CASA_DECIMAL_DIVISOR, ROUND_MODE);
            res = BigDecimal.ZERO;
        }
    }
}
