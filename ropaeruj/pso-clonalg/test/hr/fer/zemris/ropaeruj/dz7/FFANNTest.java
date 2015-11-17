package hr.fer.zemris.ropaeruj.dz7;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by ivan on 11/17/15.
 */
public class FFANNTest {
    private ParseableReadOnlyDataset dataset;

    @Before
    public void setUp() throws Exception {
        dataset =  ParseableReadOnlyDataset.createFromFile("iris.data");

    }

    @Test
    public void testCalcOutputsA() throws Exception {
        FFANN ffann = new FFANN(
                new int[]{4,3,3},
                new ITransferFunction[]{
                        //new SigmoidTransferFunction(),
                        new SigmoidTransferFunction(),
                        new SigmoidTransferFunction(),
                }
        );

        checkFann(ffann,-0.2, 0.7019685477806382);
    }

    @Test
    public void testCalcOutputsB() throws Exception {
        FFANN ffann = new FFANN(
                new int[]{4,5,3,3},
                new ITransferFunction[]{
                        new SigmoidTransferFunction(),
                        new SigmoidTransferFunction(),
                        new SigmoidTransferFunction(),
                }
        );

        checkFann(ffann,0.1,  0.8566740399081082);
    }

    private void checkFann(FFANN ffann, double weightsVals, double expected) {
        PSOTrainer trainer = new PSOGlobalTrainer(ffann, dataset, 1, 1, 1, 1, 1);

        System.out.println(dataset.getSize());

        double[] weights = new double[ffann.getWeightsCount()];
        Arrays.fill(weights, weightsVals);
        assertEquals(expected,trainer.evaluate(weights),0.05);
    }
}