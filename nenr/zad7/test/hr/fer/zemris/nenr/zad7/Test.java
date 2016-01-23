package hr.fer.zemris.nenr.zad7;

import java.io.IOException;

/**
 * Created by ivan on 1/23/16.
 */
public class Test {
    @org.junit.Test
    public void test283() throws Exception {
        test(new int[]{2,8,4,3});
    }
    @org.junit.Test
    public void test2843() throws Exception {
        test(new int[]{2,8,4,3});
    }
    @org.junit.Test
    public void test2643() throws Exception {
        test(new int[]{2,6,4,3});
    }

    private void test(int[] layers) throws IOException {
        IReadOnlyDataset dataset = ParseableReadOnlyDataset.createFromFile("data.txt");

        FunkyNeuralNetwork funkyNeuralNetwork = new FunkyNeuralNetwork(layers);


        IFANNTrainer trainer = new GATrainer(funkyNeuralNetwork,dataset,10,1000000,0.9,1,0.2, 0.02);
        trainer.trainFFANN();
    }
}
