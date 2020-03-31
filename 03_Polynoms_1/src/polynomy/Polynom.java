/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package polynomy;

import java.util.Arrays;

/**
 * Nemenne immutable
 * @author janka
 */
public class Polynom {
    //5x3 + 2x2 + 3
    
    //0 1 2 3 //indexy reprezentuju exponenty
    //3 0 2 5 //hodnoty reprezentuju koeficienty
    
    //data
    private double[] coef;
    
    //constructor
    //5 2 0 3
    //[5, 2, 0, 3]
    //3 0 2 5
    //[3, 0, 2, 5]
    //5x3 + 2x2 + 3
    
    private Polynom(double[] coef){ //defenzivna kopia
        double[] coefTemp = new double [coef.length];
        System.arraycopy(coef, 0, coefTemp, 0, coef.length);
        this.coef = coefTemp;
    }
    
    //tovarni factory metoda
    public static Polynom getInstanceReverted(double...coef){ //3 0 2 5
        return new Polynom(coef);
    }
    
    public static Polynom getInstance(double[] coef){ //[5, 2, 0, 3]
        double[] coefTemp = new double[coef.length];
        for (int i = 0; i < coef.length; i++){
            coefTemp[coef.length-1-i] = coef[i];
        }
        return new Polynom(coefTemp);
    }
    
    //metody
    public int getDegree(){
        return coef.length - 1;
    }
    
    public double getCoefAt(int exponent){
        return coef[exponent];
    }
    
    public double[] getCoef(){
        return Arrays.copyOf(coef, coef.length); //defenzivna kopia
    }
    
    public Polynom derivate(){
        //5x3 15x2
        double[] derivation = new double[coef.length - 1];
        for (int i = 0; i < derivation.length; i++){
            derivation[i] = coef[i+1]*(i+1);
        }
        return new Polynom(derivation);
    }
    
    //TODO
    public double computeValue(double x){
        //x2 + 2 ; pre x 3 bude hodnota 11
        return 0;
    }
    
    //TODO bonus
    public double integrate(double a, double b){
       return 0; 
    }
    
    //TODO vypsat matematicky spravne
    @Override
    public String toString(){ 
        //5x^2
        return Arrays.toString(coef); 
    }
    
    public static void main(String[] args) {
        double [] coef = {3, 0, 2, 5};
        Polynom p = Polynom.getInstanceReverted(coef);
        System.out.println(p);
        System.out.println(p.derivate());
    }
    
}
