/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsc.sistemaslineares.exatos;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

/**
 *
 * @author Vitor
 */
public class FatoracaoLU {
    private static final double matrizDouble[][] = {
         {3, 2, 4, 1},
        {1, 1, 2, 2},
        {4, 3, 2, 3}
    };
    private static BigDecimal matriz[][];
    private static BigDecimal pivo;
    private static int linhaAtual;
    private static int colunaAtual;
    private static ArrayList<BigDecimal> mnnArray;
    private static BigDecimal vetb[];
    private static BigDecimal vetx[];
    private static BigDecimal matL[][];
    private static BigDecimal matU[][];
    private static BigDecimal vety[];
    private static ArrayList<BigDecimal> mnnTotal;
    private static int n = 0;
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
        n = 0;
        pivo = BigDecimal.ZERO;
        linhaAtual = -1;
        colunaAtual = -1;
        mnnTotal = new ArrayList<>();
        matU = new BigDecimal[matriz.length][matriz[0].length - 1];
        for (int l = 0; l < matriz.length; l++) {
            for (int c = 0; c < matriz[0].length - 1; c++) {
                matU[l][c] = matriz[l][c];
            } 
        }
        matL = new BigDecimal[matriz.length][matriz[0].length - 1];
        vety = new BigDecimal[matriz[0].length];
        mostrarMatriz();
        System.out.println("\n");
        do {
            n++;
            linhaAtual++;
            colunaAtual++;
            pivo = matU[linhaAtual][linhaAtual];
            mnnArray = new ArrayList<>();
            criarMatrizU(linhaAtual, colunaAtual);
            mostrarTodasAsInformacoes();
            System.out.println("\n");
        } while (linhaAtual < matU.length - 1);
        criarMatrizL();
        mostrarVetorX();
    }

    private static void mostrarTodasAsInformacoes() {
        System.out.println("Nº: " + n);
        System.out.println("Pivô: " + pivo);

        for (int x = 0; x < mnnArray.size(); x++) {
            System.out.println("m[" + (x + 1) + "][" + colunaAtual + "]: " + mnnArray.get(x));
        }

       mostrarMatrizU();
    }

    private static void mostrarMatriz() {
        System.out.println("Matriz:");
        for (int x = 0; x < matriz.length; x++) {
            for (int y = 0; y < matriz[x].length; y++) {
                System.out.print(" " + matriz[x][y] + " ");
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
        mostrarMatrizU();
        mostrarMatrizL();
        
        calcularLyb();
        mostrarVetY();
        
        calcularUxy();
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
    
    
    private static void criarMatrizU(int linhaAtual, int colunaAtual){       
        BigDecimal mnnf;
        for (int l = linhaAtual + 1; l < matU.length; l++) {
            mnnf = matU[l][colunaAtual].divide(pivo, CASA_DECIMAL_DIVISOR, ROUND_MODE);
            mnnArray.add(mnnf);
            mnnTotal.add(mnnf);
                for (int c = colunaAtual; c < matU[l].length; c++) {
                    matU[l][c] = matU[l][c].subtract(mnnf.multiply(matU[linhaAtual][c]).setScale(CASA_DECIMAL, ROUND_MODE)).setScale(CASA_DECIMAL, ROUND_MODE);
                }
                
            }
    }
    
    private static void mostrarMatrizU(){
        System.out.println("\n");
        System.out.println("Matriz U:");
        for(int l = 0; l < matU.length; l++){
            for(int c = 0; c < matU[0].length; c++){
                System.out.print(" " + matU[l][c] + " ");
            }
            System.out.println("");
        }
    }
    
    private static void criarMatrizL(){
        
        int ctrl = 0;
        for (int l = 0; l < matL.length; l++) {
            for (int c = 0; c < matL[0].length; c++) {
                if(l > c){
                    matL[l][c] = mnnTotal.get(ctrl);
                    ctrl++;
                }else if(l == c){
                    matL[l][c] = BigDecimal.ONE;
                }else{
                    matL[l][c] = BigDecimal.ZERO;
                }
            } 
        }
    }
    
    private static void mostrarMatrizL(){
        System.out.println("\n");
        System.out.println("Matriz L:");
        for(int l = 0; l < matL.length; l++){
            for(int c = 0; c < matL[0].length; c++){
                System.out.print(" " + matL[l][c] + " ");
            }
            System.out.println("");
        }
    }
    
    private static void calcularLyb(){
        BigDecimal res = BigDecimal.ZERO;
        for(int l = 0; l < matL.length; l++){
            for(int c = 0; c < matL[0].length; c++){
                if(l == c){
                    break;
                }else{
                    res = res.add(matL[l][c].multiply(vety[c]).setScale(CASA_DECIMAL, ROUND_MODE)).setScale(CASA_DECIMAL, ROUND_MODE) ;
                }   
            }
            vety[l] = (vetb[l].subtract(res).setScale(CASA_DECIMAL, ROUND_MODE)).divide(matL[l][l], CASA_DECIMAL_DIVISOR, ROUND_MODE);
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
    
    private static void calcularUxy(){
        BigDecimal res = BigDecimal.ZERO;
        for(int l = matU.length - 1; l >= 0; l--){
            for(int c = matU[0].length - 1; c >= 0; c--){
                if(l == c){
                    break;
                }else{
                    res = res.add(matU[l][c].multiply(vetx[c]).setScale(CASA_DECIMAL, ROUND_MODE)).setScale(CASA_DECIMAL, ROUND_MODE);
                }   
            }
            vetx[l] = (vety[l].subtract(res).setScale(CASA_DECIMAL, ROUND_MODE)).divide(matU[l][l], CASA_DECIMAL_DIVISOR, ROUND_MODE);
            res = BigDecimal.ZERO;
        }
    }
    
}
