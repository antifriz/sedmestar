package hr.fer.zemris.ropaeruj.dz7;

import java.io.IOException;

public class Main {

    enum Algorithm {
        PSO_A,
        PSO_B,
        CLONALG
    }

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

        PSOTrainer trainer = new PSOTrainer(ffann, dataset, 1, 1,Integer.valueOf(args[2]), Double.valueOf(args[3]),Integer.valueOf(args[4]));

        trainer.trainFFANN();

        String alg = args[1];
        Algorithm algorithm;
        int neighborhood = 0;

        switch (alg) {
            case "pso-a":
                algorithm = Algorithm.PSO_A;
                break;
            case "clonalg":
                algorithm = Algorithm.CLONALG;
                break;
            default:
                if (alg.startsWith("pso-b")) {
                    String[] v = alg.split("-");
                    neighborhood = Integer.valueOf(v[v.length - 1]);
                    break;
                }
                throw new RuntimeException("Invalid arguments");
        }
        //System.out.println(Arrays.deepToString(dataset));
    }
}
