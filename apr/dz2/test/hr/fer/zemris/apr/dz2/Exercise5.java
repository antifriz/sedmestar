package hr.fer.zemris.apr.dz2;

import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by ivan on 11/8/15.
 */
public class Exercise5 {

    @Test
    public void testA() {
        int counter = 0;
        int n = 1000;
        for (int i = 0; i < n; i++) {
            ThreadLocalRandom random = ThreadLocalRandom.current();
            Point of = Point.of(random.nextDouble(-50, 50), random.nextDouble(-50, 50));
            if(run(of)){
                counter++;
            }
        }
        System.out.println(counter/(double)n*100+"%");
    }

    private boolean run(Point startingPoint) {
        AbstractFunctionToOptimize function = Functions.get(4);

        NelderMeadSimplex simplex = new NelderMeadSimplex();
        simplex.verbose=false;
        simplex.timeout= 1000;
        simplex.simplexT=50;
        Point ps;
        try{
            ps = simplex.findMinimum(function, startingPoint);


        int dimension = startingPoint.getDimension();
        //System.out.printf("NMS: %5d %s %6.4f\n", simplex.lastIterationCount, ps, PointUtils.deviation(ps, function.minimumAt(dimension)));
        return Math.abs(function.minimumValue()-function.valueAt(ps))<=0.0001;
        }
        catch (Throwable e){
            return false;
        }
    }

}
