/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsc.principal;

import br.edu.ifsc.ajustedecurva.AjusteLinear;
import br.edu.ifsc.ajustedecurva.AjusteLinearMultiplo;
import br.edu.ifsc.ajustedecurva.AjustePolinomial;

/**
 *
 * @author Vitor
 */
public class Principal {
    public static void main(String[] args) {
        //AjusteLinear.calcularD();
        //AjusteLinear.calcularYAtravesDeAjuste(175);
        //AjusteLinearMultiplo.calcularVetorBeta();
        //AjusteLinearMultiplo.calcularYcomAjusteDeCurva(1);
        AjustePolinomial.calcularVetorBeta(3);
        //AjustePolinomial.calcularYcomAjusteDeCurva(1);
    }
}
