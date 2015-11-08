package hr.fer.zemris.apr.dz2;

import hr.fer.zemris.apr.dz2.Config;
import hr.fer.zemris.apr.dz2.Point;
import hr.fer.zemris.apr.dz2.PointUtils;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by ivan on 11/8/15.
 */
public class PointUtilsTest {
    @Test
    public void testSimplex() throws Exception {

        double t = 0.6;
        Point of = Point.of(0, 0, 1);
        List<Point> simplex = PointUtils.constructSimplex(of, t);

        assertEquals((simplex.size()-1)*t,simplex.stream().map(x->x.minus(of)).reduce(Point::plus).map(Point::sum).get(),0.01);
//
//        for (int i = 0; i < simplex.size(); i++) {
//            for (int j = i+1; j < simplex.size(); j++) {
//                double distance = simplex.get(i).distanceTo(simplex.get(j));
//                assertEquals(t,distance, Config.PRECISION_9);
//            }
//        }

    }
}
