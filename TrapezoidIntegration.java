package numericalmethods;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Savvas on 6/12/2017.
 */
public class TrapezoidIntegration {

    Function function;
    double a; //left end of interval
    double b; //right end of interval
    double n; //number of steps
    double h;
    double sum; //trapezoid sum
    List<Double> xs;
    List<Double> ys;

    public TrapezoidIntegration(double a, double b, double n,Function function){
        this.function = function;
        this.a = a;
        this.b = b;
        this.n = n;
        this.h = (b - a)/n;
        xs = new LinkedList<>(); //list with x values
        ys = new LinkedList<>(); //list with f(x) values
        setXs();
        setYs();
    }

    private void setXs(){

        for(int i=0; i<n+1; i++){
            xs.add(i,a + h*i);
        }
    }

    private void setYs(){

        for(int i=0; i<n+1; i++){
            ys.add(i,function.getY(xs.get(i)));
        }
    }

    public double integrate(){

        for(int i=0; i<n-1; i++){
            sum = sum + ((xs.get(i+1) - xs.get(i))*(ys.get(i) + ys.get(i+1))/2);
        }

        return sum;
    }


}
