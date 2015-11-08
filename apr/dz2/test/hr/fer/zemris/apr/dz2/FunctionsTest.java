package hr.fer.zemris.apr.dz2;

import hr.fer.zemris.apr.dz2.Config;
import hr.fer.zemris.apr.dz2.Functions;
import hr.fer.zemris.apr.dz2.IFunctionToOptimize;
import hr.fer.zemris.apr.dz2.Point;
import org.junit.Test;

/**
 * Created by ivan on 11/8/15.
 */
public class FunctionsTest {
    @Test
    public void testFunctions() throws Exception {
        for (int i = 0; i < Functions.size(); i++) {
            IFunctionToOptimize f = Functions.get(i);

            int dimension = f.dimension(3);
            Point minimumPoint = f.minimumAt(dimension);

            double calculatedMinumum = f.valueAt(minimumPoint);
            double expectedMinumum = f.minimumValue();


            org.junit.Assert.assertEquals(String.format("%d %s",i, minimumPoint), expectedMinumum, calculatedMinumum, Config.PRECISION_9);
        }
    }
}
