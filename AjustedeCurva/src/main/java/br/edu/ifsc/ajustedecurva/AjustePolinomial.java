/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsc.ajustedecurva;

import br.edu.ifsc.sistemaslineares.exatos.EliminacaoGaussiana;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author Vitor
 */
public class AjustePolinomial {

    private static BigDecimal matXi[][];
    private static BigDecimal vetYi[];
    private static BigDecimal vetB[];
    private static BigDecimal vetSomaYiXi[];

    private static final RoundingMode ROUND_MODE = RoundingMode.HALF_EVEN;
    private static final int CASA_DECIMAL = 3;
    private static final int CASA_DECIMAL_DIVISOR = 15;

    private static void criarVetoresBigDecimal() {
        double matx[][] = {
            {-1, 0, 1, 2, 4, 5, 5, 6},
            {-2, -1, 0, 1, 1, 2, 3, 4}
        };
        double vety[] = {13, 11, 9, 4, 11, 9, 1, -1};

        matXi = new BigDecimal[matx.length][matx[0].length];
        vetYi = new BigDecimal[vety.length];

        for (int l = 0; l < matXi.length; l++) {
            for (int c = 0; c < matXi[0].length; c++) {
                matXi[l][c] = new BigDecimal(Double.toString(matx[l][c])).setScale(CASA_DECIMAL, ROUND_MODE);
            }
        }

        for (int x = 0; x < vetYi.length; x++) {
            vetYi[x] = new BigDecimal(Double.toString(vety[x])).setScale(CASA_DECIMAL, ROUND_MODE);
        }
    }

    public static void calcularVetorBeta() {
        criarVetoresBigDecimal();
        calcularB();
        calcularCoeficienteDeDeterminacao();
    }

    private static void calcularB() {
        int n = matXi[0].length;
        BigDecimal somaXiP;
        BigDecimal somaYi;
        BigDecimal somaXiPYi;
        BigDecimal matRes[][] = new BigDecimal[matXi.length + 1][matXi.length + 1];
        BigDecimal N = new BigDecimal(n).setScale(CASA_DECIMAL, ROUND_MODE);

        for (int lr = 0; lr < matRes.length; lr++) {
            for (int cr = 0; cr < matRes[0].length; cr++) {
                if(lr == 0 && cr == 0){
                    matRes[lr][cr] = N;
                }else if (lr <= cr) {
                    somaXiP = BigDecimal.ZERO;
                        for (int c = 0; c < matXi[0].length; c++) {
                            somaXiP = somaXiP.add((matXi[cr -1 ][c].pow(cr))).setScale(CASA_DECIMAL, ROUND_MODE);
                            System.out.println("\n");
                            System.out.println("x[" + (cr - 1)  + "][" + c + "] = " + (matXi[cr - 1][c]).setScale(CASA_DECIMAL, ROUND_MODE));
                            System.out.println("x[" + (cr - 1) + "][" + c + "] ^ (" + cr + ") = " + (matXi[cr - 1][c]).pow(cr).setScale(CASA_DECIMAL, ROUND_MODE));
                            System.out.println("Soma x[" + (cr - 1 ) + "] ^ (" + cr +") = " + somaXiP);
                        }
                    matRes[lr][cr] = somaXiP;
                    System.out.println("====================");
                }

            }
        }

        for (int lr = 0; lr < matRes.length; lr++) {
            for (int cr = 0; cr < matRes[0].length; cr++) {
                matRes[cr][lr] = matRes[lr][cr];
            }
        }

        System.out.println("\n");
        System.out.println("n: " + n);
        System.out.println("Matrís de Somatórios: ");
        for (int l = 0; l < matRes.length; l++) {
            for (int c = 0; c < matRes[0].length; c++) {
                System.out.print(" " + matRes[l][c] + " ");
            }
            System.out.println("");
        }

        somaYi = BigDecimal.ZERO;
        System.out.println("Somatário Yi:");
        for (int x = 0; x < vetYi.length; x++) {
            somaYi = somaYi.add(vetYi[x]).setScale(CASA_DECIMAL, ROUND_MODE);
            System.out.println("\n");
            System.out.println("y[" + x + "] = " + vetYi[x]);
            System.out.println("Somatório y: " + somaYi);

        }
        System.out.println("====================");
        System.out.println("\n");

        vetSomaYiXi = new BigDecimal[matXi.length + 1];
        vetSomaYiXi[0] = somaYi;

        somaXiPYi = BigDecimal.ZERO;
        System.out.println("Somatório Xi^(P) * Yi:");
        for (int l = 0; l < matXi.length; l++) {
            for (int c = 0; c < matXi[0].length; c++) {
                somaXiPYi = somaXiPYi.add(matXi[l][c].multiply(vetYi[c])).setScale(CASA_DECIMAL, ROUND_MODE);
                System.out.println("\n");
                System.out.println("x[" + l + "][" + c + "] = " + matXi[l][c]);
                System.out.println("y[" + c + "] = " + vetYi[c]);
                System.out.println("x[" + l + "][" + c + "] * y[" + c + "] = " + matXi[l][c].multiply(vetYi[c]).setScale(CASA_DECIMAL, ROUND_MODE));
                System.out.println("Soma x[" + l + "] * y[" + c + "] = " + somaXiPYi);

            }
            vetSomaYiXi[l + 1] = somaXiPYi;
            somaXiPYi = BigDecimal.ZERO;
            System.out.println("====================");
        }

        System.out.println("Vetor de Somatórios Y:");
        for (int x = 0; x < vetSomaYiXi.length; x++) {
            System.out.println(vetSomaYiXi[x]);
        }

        System.out.println("\n");
        BigDecimal matXiSomaYi[][] = new BigDecimal[matRes.length][matRes[0].length + 1];
        for (int l = 0; l < matRes.length; l++) {
            for (int c = 0; c < matRes[0].length; c++) {
                matXiSomaYi[l][c] = matRes[l][c];
            }
        }
        for (int l = 0; l < matXiSomaYi.length; l++) {
            matXiSomaYi[l][matRes.length] = vetSomaYiXi[l];
        }

        System.out.println("\n");
        System.out.println("Matrís Completa de Somatórios: ");
        for (int l = 0; l < matXiSomaYi.length; l++) {
            for (int c = 0; c < matXiSomaYi[0].length; c++) {
                System.out.print(" " + matXiSomaYi[l][c] + " ");
            }
            System.out.println("");
        }

        System.out.println("\n");
        EliminacaoGaussiana.setMatriz(matXiSomaYi);
        vetB = EliminacaoGaussiana.calcularVetorX();

        System.out.println("\n");
        System.out.println("==================================================================================");
        System.out.println("Ajuste de Curva:");
        for (int x = 0; x < vetB.length; x++) {
            System.out.print("+ (" + vetB[x] + ") * x[" + x + "] ^(" + x + ") ");
        }
        System.out.println("");
        System.out.println("==================================================================================");

    }

    public static void calcularCoeficienteDeDeterminacao() {
        BigDecimal N = new BigDecimal(matXi[0].length);
        BigDecimal vetRi[] = new BigDecimal[matXi[0].length];
        BigDecimal somaYiRi2;
        BigDecimal somaYi2;
        BigDecimal somaYi;
        BigDecimal somaRi = vetB[0];
        System.out.println("\n");
        System.out.println("Coeficiente de Determinação:");
        for (int c = 0; c < matXi[0].length; c++) {
            for (int l = 0; l < matXi.length; l++) {
                somaRi = somaRi.add(vetB[l + 1].multiply(matXi[l][c])).setScale(CASA_DECIMAL, ROUND_MODE);
                System.out.println("\n");
                System.out.println("B[" + (l + 1) + "] = " + vetB[l + 1]);
                System.out.println("x[" + l + "][" + c + "] = " + matXi[l][c]);
                System.out.println("B[" + (l + 1) + "] * x[" + l + "][" + c + "] = " + vetB[l + 1].multiply(matXi[l][c]).setScale(CASA_DECIMAL, ROUND_MODE));
            }
            vetRi[c] = somaRi;
            System.out.println("^y[" + c + "] = " + somaRi);
            somaRi = vetB[0];
            System.out.println("============================");
        }
        System.out.println("\n");
        System.out.println("===============================================");
        System.out.println("Vetor ^Yi:");
        for (int x = 0; x < vetRi.length; x++) {
            System.out.println("^y[" + x + "] = " + vetRi[x]);
        }

        System.out.println("===============================================");
        System.out.println("\n");

        somaYiRi2 = BigDecimal.ZERO;
        System.out.println("Somatório (Yi - ^Yi) ^ 2:");
        for (int x = 0; x < vetYi.length; x++) {
            somaYiRi2 = somaYiRi2.add((vetYi[x].subtract(vetRi[x])).pow(2).setScale(CASA_DECIMAL, ROUND_MODE));
            System.out.println("(y[" + x + "] -  ^y[" + x + "])^2 = " + (vetYi[x].subtract(vetRi[x])).pow(2).setScale(CASA_DECIMAL, ROUND_MODE));
        }
        System.out.println("Somatório (Yi - ^Yi) ^ 2 = " + somaYiRi2);
        System.out.println("===============================================");
        System.out.println("\n");

        somaYi2 = BigDecimal.ZERO;
        System.out.println("Somatório (Yi)^2:");
        for (int x = 0; x < vetYi.length; x++) {
            somaYi2 = somaYi2.add((vetYi[x].pow(2)).setScale(CASA_DECIMAL, ROUND_MODE));
            System.out.println("(y[" + x + "])^2 = " + (vetYi[x].pow(2).setScale(CASA_DECIMAL, ROUND_MODE)));
        }
        System.out.println("Somatório (Yi) ^ 2 = " + somaYiRi2);
        System.out.println("===============================================");
        System.out.println("\n");

        somaYi = BigDecimal.ZERO;
        System.out.println("Somatário Yi:");
        for (int x = 0; x < vetYi.length; x++) {
            somaYi = somaYi.add(vetYi[x]).setScale(CASA_DECIMAL, ROUND_MODE);
            System.out.println("y[" + x + "] = " + vetYi[x]);
        }
        System.out.println("Somatório y: " + somaYi);
        System.out.println("===================================================================");
        System.out.println("\n");

        System.out.println("Coeficiente de Determinação:");
        BigDecimal coeficiente = BigDecimal.ONE.
                subtract(somaYiRi2.divide(somaYi2.
                                subtract(somaYi.pow(2).divide(N, CASA_DECIMAL_DIVISOR, ROUND_MODE)),
                                CASA_DECIMAL_DIVISOR, ROUND_MODE));
        System.out.println("R^2 = " + coeficiente);
    }
    
    public static void calcularYcomAjusteDeCurva(double valor){
        BigDecimal bigValor = new BigDecimal(Double.toString(valor));
        System.out.println("\n");
        System.out.println("Resultado do calculo para x = " + bigValor + ":");
        BigDecimal resultado = BigDecimal.ZERO;
         for (int x = 0; x < vetB.length; x++) {
            System.out.print("+ (" + vetB[x] + ") * " + bigValor + " ^(" + x + ") ");
            resultado = resultado.add(vetB[x].multiply(bigValor.pow(x))).setScale(CASA_DECIMAL, ROUND_MODE);
        }
        System.out.println("= " + resultado);
        System.out.println("");
    }
}
