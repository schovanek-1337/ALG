
package polynoms;

/**
 * Knihovni trida (Library class)
 * @author janvit
 */
public class PolynomsOperations {

    private PolynomsOperations(){ //aby nebylo mozne vytvorit objekt, neni jej potreba

    }

    //6 0 3 5      5x3 + 3x2      + 6
    //1 3 6              6x2 + 3x + 1
    //7 3 9 5      5x3 + 9x2 + 3x + 7
    public static Polynom sum(Polynom a, Polynom b){
        Polynom max, min;
        if (a.getDegree() > b.getDegree()) {max = a; min = b;}
        else {max = b; min = a;}

        double[] sumCoef = new double[max.getDegree() + 1];
        for (int i = 0; i <= min.getDegree(); i++) {
            sumCoef[i] = max.getCoefAt(i) + min.getCoefAt(i);
        }
        for (int i = min.getDegree() + 1; i <= max.getDegree(); i++) {
            sumCoef[i] = max.getCoefAt(i);
        }
        return Polynom.getInstanceReverted(sumCoef);
    }

    //TODO
    public static Polynom multiply(Polynom a, Polynom b){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    //TODO bonus
    public static Polynom sumAll(Polynom...polynoms){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    //TODO bonus
    public static Polynom multiplyAll(Polynom...polynoms){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public static void main(String[] args) {
        Polynom p1 = Polynom.getInstance(5, 3, 0, 6);
        Polynom p2 = Polynom.getInstance(6, 3, 1);
        System.out.println(PolynomsOperations.sum(p1, p2));
    }

}
