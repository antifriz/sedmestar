package hr.fer.zemris.ropaeruj.dz7;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;

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
                new int[]{4,5,3,3},
                new ITransferFunction[]{
                        new SigmoidTransferFunction(),
                        new SigmoidTransferFunction(),
                        new SigmoidTransferFunction()
                },
                dataset
        );

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
                    neighborhood = Integer.valueOf(v[v.length-1]);
                    break;
                }
                throw new RuntimeException("Invalid arguments");
        }

        int n = Integer.valueOf(args[2]);

        double merr = Double.valueOf(args[3]);

        int maxiter = Integer.valueOf(args[4]);
        System.out.println(Arrays.deepToString(dataset));
    }
}
