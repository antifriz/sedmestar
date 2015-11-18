package hr.fer.zemris.ropaeruj.dz7;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Created by ivan on 11/17/15.
 */
public class FFANNTest {
    private ParseableReadOnlyDataset dataset;

    @Before
    public void setUp() throws Exception {
        dataset = ParseableReadOnlyDataset.createFromFile("iris.data");
    }

    @Test
    public void testCalcOutputsA() throws Exception {
        checkFann(FFANN.createSigmoidal(new int[]{4, 3, 3}, dataset), -0.2, 0.7019685477806382);
    }

    @Test
    public void testCalcOutputsB() throws Exception {
        checkFann(FFANN.createSigmoidal(new int[]{4, 5, 3, 3}, dataset), 0.1, 0.8566740399081082);
    }

    private void checkFann(FFANN ffann, double weightsVals, double expected) {
        System.out.println(dataset.getSize());

        double[] weights = new double[ffann.getWeightsCount()];
        Arrays.fill(weights, weightsVals);
        assertEquals(expected, ffann.evaluate(weights), 0.05);
    }
}