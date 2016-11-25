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
public class PivoteamentoParcial {

    private static final double matrizDouble[][] = {
        {3.0, 2.0, 4.0, 1.0},
        {1.0, 1.0, 2.0, 2.0},
        {4.0, 3.0, -2.0, 3.0}
    };
    
    private static BigDecimal matriz[][];
    private static BigDecimal pivo;
    private static int linhaAtual;
    private static int colunaAtual;
    private static ArrayList<BigDecimal> mnnArray;
    private static BigDecimal vetb[];
    private static BigDecimal vetx[];
    private static BigDecimal matA[][];
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
        
        mostrarMatriz();
        
        System.out.println("\n");
        do {
            n++;
            linhaAtual++;
            colunaAtual++;
            pivoteamentoParcial();
            pivo = matriz[linhaAtual][linhaAtual];
            mnnArray = new ArrayList<>();
            BigDecimal mnn;
            for (int l = linhaAtual + 1; l < matriz.length; l++) {
                mnn = matriz[l][colunaAtual].divide(pivo, CASA_DECIMAL_DIVISOR, ROUND_MODE);
                mnnArray.add(mnn);
                for (int c = colunaAtual; c < matriz[0].length; c++) {             
                        matriz[l][c] = matriz[l][c].subtract(mnn.multiply(matriz[linhaAtual][c]).setScale(CASA_DECIMAL, ROUND_MODE)).setScale(CASA_DECIMAL, ROUND_MODE);
                             
                }
                
            }

            mostrarTodasAsInformacoes();
            System.out.println("\n");
        } while (linhaAtual < matriz.length - 1);
        mostrarVetorX();
    }

    private static void mostrarTodasAsInformacoes() {
        System.out.println("Nº: " + n);
        System.out.println("Pivô: " + pivo);

        for (int x = 0; x < mnnArray.size(); x++) {
            System.out.println("m[" + (x + 1) + "][" + colunaAtual + "]: " + mnnArray.get(x));
        }

        System.out.println("Matriz:");
        for (int x = 0; x < matriz.length; x++) {
            for (int y = 0; y < matriz[0].length; y++) {
                System.out.print(" " + matriz[x][y] + " ");
            }
            System.out.println("");
        }
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
        BigDecimal  res = BigDecimal.ZERO;
        vetx = new BigDecimal[matriz.length];
        vetb = new BigDecimal[matriz.length];
        matA = new BigDecimal[matriz.length][matriz[0].length - 1];
        
        
        
        for(int b = 0; b < vetb.length; b++){
            vetb[b] = matriz[b][matriz[0].length -1];
        }
        
        
        for (int l = 0; l < matriz.length; l++) {
            for (int c = 0; c < matriz[0].length - 1; c++) {
                matA[l][c] = matriz[l][c];
            } 
        }
        
        mostrarVetorB();
        mostrarMatrizA();
        
        for(int l = matA.length - 1; l >= 0; l--){
            for(int c = matA[0].length - 1; c >= 0; c--){
                if(l == c){
                    break;
                }else{
                    res = res.add(matA[l][c].multiply(vetx[c]).setScale(CASA_DECIMAL, ROUND_MODE)).setScale(CASA_DECIMAL, ROUND_MODE);
                }   
            }
            vetx[l] = (vetb[l].subtract(res).setScale(CASA_DECIMAL, ROUND_MODE)).divide(matA[l][l], CASA_DECIMAL_DIVISOR, ROUND_MODE);
            res = BigDecimal.ZERO;
        }
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
    
    private static void mostrarMatrizA(){
        System.out.println("\n");
        System.out.println("Matriz A:");
        for(int l = 0; l < matA.length; l++){
            for(int c = 0; c < matA[0].length; c++){
                System.out.print(" " + matA[l][c] + " ");
            }
            System.out.println("");
        }
    }
    
    
    public static void mostrarSomaVetorX(){
        System.out.println("\n");
        BigDecimal soma = BigDecimal.ZERO;
        for(int x = 0; x < vetx.length; x++){
            soma = soma.add(vetx[x]);
        }
        System.out.println("Soma de todos os Xs: " + soma);
    }

    private static void pivoteamentoParcial() {
        BigDecimal maiorEmModulo = BigDecimal.ZERO;
        int linha = linhaAtual;
        for (int w = linhaAtual; w < matriz[linhaAtual].length - 1; w++) {
            if (maiorEmModulo.compareTo(matriz[w][colunaAtual]) == -1) {
                maiorEmModulo = matriz[w][colunaAtual];
                linha = w;
            }
        }
        if (linha != linhaAtual) {
            BigDecimal aux[] = new BigDecimal[matriz[linhaAtual].length];
            for (int a = 0; a < matriz[linhaAtual].length; a++) {
                aux[a] = matriz[linhaAtual][a];
                matriz[linhaAtual][a] = matriz[linha][a];
                matriz[linha][a] = aux[a];
            }
        }

    }
    
   
}
