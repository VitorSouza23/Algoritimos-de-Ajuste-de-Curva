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

    private static BigDecimal vetXi[];
    private static BigDecimal vetYi[];
    private static BigDecimal vetB[];
    private static BigDecimal vetSomaYiXi[];

    private static final RoundingMode ROUND_MODE = RoundingMode.HALF_EVEN;
    private static final int CASA_DECIMAL = 3;
    private static final int CASA_DECIMAL_DIVISOR = 15;

    private static void criarVetoresBigDecimal() {
        double vetx[] = {-5,-4,-2,0,1,2,3,5};
        double vety[] = {386,255,54,6,13,40,110,220};

        vetXi = new BigDecimal[vetx.length];
        vetYi = new BigDecimal[vety.length];

        for (int l = 0; l < vetXi.length; l++) {
            vetXi[l] = new BigDecimal(Double.toString(vetx[l])).setScale(CASA_DECIMAL, ROUND_MODE);
        }

        for (int x = 0; x < vetYi.length; x++) {
            vetYi[x] = new BigDecimal(Double.toString(vety[x])).setScale(CASA_DECIMAL, ROUND_MODE);
        }
    }

    public static void calcularVetorBeta(int p) {
        criarVetoresBigDecimal();
        calcularB(p);
        calcularCoeficienteDeDeterminacao();
    }

    private static void calcularB(int p) {
        int n = vetXi.length;
        BigDecimal somaXiP;
        BigDecimal somaYi;
        BigDecimal somaXiPYi;
        BigDecimal matRes[][] = new BigDecimal[p + 1][p + 1];
        BigDecimal N = new BigDecimal(n).setScale(CASA_DECIMAL, ROUND_MODE);

        for (int lr = 0; lr < matRes.length; lr++) {
            for (int cr = 0; cr < matRes[0].length; cr++) {
                if (lr <= cr) {
                    somaXiP = BigDecimal.ZERO;
                    for (int c = 0; c < vetXi.length; c++) {
                        somaXiP = somaXiP.add((vetXi[c].pow(cr + lr))).setScale(CASA_DECIMAL, ROUND_MODE);
                        System.out.println("\n");
                        System.out.println("x[" + c + "] = " + (vetXi[c]).setScale(CASA_DECIMAL, ROUND_MODE));
                        System.out.println("x[" + c + "] ^ (" + (cr + lr) + ") = " + (vetXi[c]).pow(cr + lr).setScale(CASA_DECIMAL, ROUND_MODE));
                        System.out.println("Soma x[" + (c) + "] ^ (" + (cr + lr) + ") = " + somaXiP);
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

        vetSomaYiXi = new BigDecimal[matRes.length];
        vetSomaYiXi[0] = BigDecimal.ZERO;

        somaXiPYi = BigDecimal.ZERO;
        System.out.println("Somatório Xi^(P) * Yi:");

        for (int y = 0; y < vetSomaYiXi.length; y++) {
            for (int l = 0; l < vetXi.length; l++) {
                somaXiPYi = somaXiPYi.add(vetXi[l].pow(y).multiply(vetYi[l])).setScale(CASA_DECIMAL, ROUND_MODE);
                System.out.println("\n");
                System.out.println("x[" + l + "] = " + vetXi[l]);
                System.out.println("y[" + l + "] = " + vetYi[l]);
                System.out.println("x[" + l + "]^(" + y + ") * y[" + l + "] = " + vetXi[l].pow(y).multiply(vetYi[l]).setScale(CASA_DECIMAL, ROUND_MODE));
                System.out.println("Soma x[" + l + "] * y[" + l + "] = " + somaXiPYi);

            }
            vetSomaYiXi[y] = somaXiPYi;
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
        BigDecimal N = new BigDecimal(vetXi.length);
        BigDecimal vetRi[] = new BigDecimal[vetXi.length];
        BigDecimal somaYiRi2;
        BigDecimal somaYi2;
        BigDecimal somaYi;
        BigDecimal somaRi = BigDecimal.ZERO;
        System.out.println("\n");
        System.out.println("Coeficiente de Determinação:");

        for (int l = 0; l < vetXi.length; l++) {
            for (int b = 0; b < vetB.length; b++) {
                somaRi = somaRi.add(vetB[b].multiply(vetXi[l].pow(b))).setScale(CASA_DECIMAL, ROUND_MODE);
                System.out.println("\n");
                System.out.println("B[" + (b) + "] = " + vetB[b]);
                System.out.println("x[" + l + "] = " + vetXi[l]);
                System.out.println("x[" + l + "]^(" + b + ") = " + vetXi[l].pow(b).setScale(CASA_DECIMAL, ROUND_MODE));
                System.out.println("B[" + b + "] * x[" + l + "]^(" + b + ") = " + vetB[b].multiply(vetXi[l].pow(b)).setScale(CASA_DECIMAL, ROUND_MODE));
            }
            vetRi[l] = somaRi;
            System.out.println("^y[" + l + "] = " + somaRi);
            somaRi = BigDecimal.ZERO;
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
        System.out.println("Somatório (Yi) ^ 2 = " + somaYi2);
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

    public static void calcularYcomAjusteDeCurva(double valor) {
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
