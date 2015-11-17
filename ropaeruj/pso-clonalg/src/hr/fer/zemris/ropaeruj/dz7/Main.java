package hr.fer.zemris.ropaeruj.dz7;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        // write your code here

        IReadOnlyDataset dataset = ParseableReadOnlyDataset.createFromFile(args[0]);


        FFANN ffann = new FFANN(
                new int[]{4, 5, 3, 3},
                new ITransferFunction[]{
                        new SigmoidTransferFunction(),
                        new SigmoidTransferFunction(),
                        new SigmoidTransferFunction()
                }
        );

        String alg = args[1];
        //int neighborhood = 0;

        IFANNTrainer trainer;

        int particleCount = Integer.valueOf(args[2]);
        double err = Double.valueOf(args[3]);
        int maxIter = Integer.valueOf(args[4]);

        switch (alg) {
            case "pso-a":
            case "clonalg":
                trainer = new PSOGlobalTrainer(ffann, dataset, particleCount, err, maxIter);
                break;
            default:
                if (alg.startsWith("pso-b")) {
                    String[] v = alg.split("-");
                    int neighborhood = Integer.valueOf(v[v.length - 1]);
                    trainer = new PSOLocalTrainer(ffann, dataset, particleCount, err, maxIter, neighborhood);
                    break;
                }
                throw new RuntimeException("Invalid arguments");
        }


        trainer.trainFFANN();
    }
}
