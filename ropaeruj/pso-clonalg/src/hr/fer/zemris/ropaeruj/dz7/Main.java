package hr.fer.zemris.ropaeruj.dz7;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        // write your code here

        IReadOnlyDataset dataset = ParseableReadOnlyDataset.createFromFile(args[0]);

        int[] layers = {4, 5, 3, 3};

        FFANN ffann = FFANN.createSigmoidal(layers, dataset);

        String alg = args[1];
        IFANNTrainer trainer;

        int particleCount = Integer.valueOf(args[2]);
        double err = Double.valueOf(args[3]);
        int maxIter = Integer.valueOf(args[4]);

        switch (alg) {
            case "pso-a":
                trainer = new PSOGlobalTrainer(ffann, particleCount, err, maxIter);
                break;
            case "clonalg":
                trainer = new ClonAlg(ffann,particleCount,err,maxIter);
                break;
            default:
                if (alg.startsWith("pso-b")) {
                    String[] v = alg.split("-");
                    int neighborhood = Integer.valueOf(v[v.length - 1]);
                    trainer = new PSOLocalTrainer(ffann, particleCount, err, maxIter, neighborhood);
                    break;
                }
                throw new RuntimeException("Invalid arguments");
        }


        trainer.trainFFANN();
    }
}
