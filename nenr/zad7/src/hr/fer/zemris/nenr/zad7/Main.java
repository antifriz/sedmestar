package hr.fer.zemris.nenr.zad7;

import java.io.IOException;

/**
 *
 * npr: iris.data pso-b-7 20 0.02 10000
 *
 */
public class Main {

    public static void main(String[] args) throws IOException {
        IReadOnlyDataset dataset = ParseableReadOnlyDataset.createFromFile("data.txt");

        int[] layers = {2,8,4,3};
        FunkyNeuralNetwork funkyNeuralNetwork = new FunkyNeuralNetwork(layers);


        IFANNTrainer trainer = new GATrainer(funkyNeuralNetwork,dataset,50,1000000,0.8,0.1,0.2, 0.02);
        trainer.trainFFANN();
    }
}
