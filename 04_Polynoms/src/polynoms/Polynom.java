package polynoms;

import java.util.Arrays;

/**
 * Represents a polynom
 * Definuje se pri vytvoreni, pak se uz nemeni - immutable object
 * @author janvit
 */
public class Polynom {
    //data - clenske instancni promenne (instance attributes, fields)
    //5x3 + 3x2 + 6  6 0 3 5

    //0 1 2 3 //indexy reprezentuju exponenty
    //6 0 3 5 //hodnoty reprezentuju koeficienty
    private double[] coef;

    //constructors
    //moznosti zadani
    //[6 0 3 5] pole jiz prevracenych koeficientu
    //[5 3 0 6] pole neprevracenych koeficientu
    //6, 0, 3, 5 prevracene koeficienty
    //5, 3, 0, 6 neprevracene koeficienty

    private Polynom(double[] coef){
        double[] coefTemp = new double[coef.length]; //defenzivni kopie, aby boly privatni i hodnoty pole
        System.arraycopy(coef, 0, coefTemp, 0, coef.length);
        this.coef = coefTemp;
    }

    //tovarni metoda (factory method)
    public static Polynom getInstanceReverted(double[] coef){ //[6 0 3 5]
        return new Polynom(coef);
    }

    public static Polynom getInstance(double...coef){ //5, 3, 0, 6
        double[] coefTemp = new double[coef.length];
        //revert coeficients
        for (int i = 0; i < coef.length; i++) {
            coefTemp[coefTemp.length - 1 - i] = coef[i];
        }
        return new Polynom(coefTemp);
    }

    //metody

    //TODO
    //5x3 + 3x2 + 6 pre x = 1; y = 5 + 3 + 6 = 14
    //pouzit Hornerovo schema
    public double computeValue(double x){
      throw new UnsupportedOperationException("Not supported yet.");
    }

    //gettre
    public double getCoefAt(int exponent){
        return coef[exponent];
    }

    public double[] getAllCoef(){ //defenzivni kopie
        return Arrays.copyOf(coef, coef.length);
    }

    public int getDegree(){
        return coef.length - 1;
    }

    //TODO vypisat matematicky spravne 5x^3 + ...
    @Override
    public String toString() {
        return Arrays.toString(coef);
    }

    //5x3 + 3x2 + 6 zderivovane bude 15x2 + 6x
    public Polynom derivate(){
        double[] coefD = new double[coef.length - 1]; //koef derivacie je o jedno mensi
        for (int i = 0; i < coefD.length; i++) {
            coefD[i] = coef[i+1]*(i + 1);
        }
        return new Polynom(coefD);
    }

    //TODO bonus
    public double integrate(double a, double b){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public static void main(String[] args) {
        double[] a = {6, 0, 3, 5};
        Polynom p1 = Polynom.getInstanceReverted(a);
        System.out.println(p1);
        System.out.println(p1.getCoefAt(3));
        System.out.println(p1.derivate());
    }
}
