package hr.fer.zemris.apr.dz2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by ivan on 11/8/15.
 */
public class PointUtils {
    public static List<Point> constructSimplex(Point initialPoint, double t) {
        int n = initialPoint.getDimension();

        double a1 = t;//t / n / Math.sqrt(2) * (Math.sqrt(n + 1) + n - 1);
        double a2 = 0;//t / n / Math.sqrt(2) * (Math.sqrt(n + 1) - 1);

        List<Point> d = new ArrayList<>(n + 1);

        d.add(initialPoint.copy());

        for (int i = 0; i < n; i++) {
            Point point = Point.zeros(n);
            point.fill(a2);
            point.set(i, a1);
            d.add(point.plus(initialPoint));
        }
        return d;
    }

    public static Point centroid(Collection<Point> d, Point without) {
        return d.stream().reduce(Point::plus).get().minus(without).multiply(1.0 / (d.size() - 1));
    }
}
