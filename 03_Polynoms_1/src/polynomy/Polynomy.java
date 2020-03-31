package polynomy;

/**
 * Library knihovni trida
 * @author janka
 */
public class Polynomy {
    
    private Polynomy(){
        
    }
    
    public static Polynom sum(Polynom a, Polynom b){ //nemusime delat defenzivni kopii, protoze Polynom je immutable
        //5x3 + 2x2     + 3       3 0 2 5
        //      4x2 + x + 2       2 1 4
        //5x3 + 6x2 + x + 5       5 1 6 5
        Polynom max = a.getDegree() > b.getDegree()? a : b;
        Polynom min = a.getDegree() <= b.getDegree()? a : b;
        double[] sum = new double[max.getDegree() + 1];
        for (int i = 0; i < sum.length ; i++) {
            sum[i] = max.getCoefAt(i);
        }
        for (int i = 0; i < min.getDegree() + 1; i++) {
            sum[i] = sum[i] + min.getCoefAt(i); 
        }
        return Polynom.getInstanceReverted(sum);
    }
    
    //TODO
    public static Polynom multiply(Polynom a, Polynom b){
        return null;
    }
    
    public static void main(String[] args) {
        Polynom p1 = Polynom.getInstanceReverted(3, 0, 2, 5);
        Polynom p2 = Polynom.getInstanceReverted(2, 1, 4);
        System.out.println(sum(p1, p2));
    }
    
}
