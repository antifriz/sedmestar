package hr.fer.zemris.nenr.zad7;

import java.io.IOException;

/**
 *
 * npr: iris.data pso-b-7 20 0.02 10000
 *
 */
public class Main {

    public static void main(String[] args) throws IOException {
        IReadOnlyDataset dataset = ParseableReadOnlyDataset.createFromFile(args[0]);

        int[] layers = {2, 3,3};

        FFANN ffann = FFANN.createSigmoidal(layers, dataset);


        IFANNTrainer trainer = new GATrainer(ffann,10,10000,0.8,0.1,1);
        trainer.trainFFANN();
    }
}
