package ann;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by ivan on 12/9/15.
 */
public class ANNTrainer {
    public static void main(String[] args) throws IOException {

        Random random = new Random();

        ANN ann = ANN.create(args[1], new SigmoidTransferFunction());

        ParseableReadOnlyDataset dataset = ParseableReadOnlyDataset.loadData(args[0], ann.getInputDimension(), 600);
        System.out.println(dataset.getSize());

        DiffEvoAlg diffEvoAlg = new DiffEvoAlg();
        diffEvoAlg.setEvaluator(ANNEvaluator.createFor(ann, dataset));


        double crossoverConstant = 0.9;

        int populationSize = Integer.parseInt(args[2]);
        double desiredError = Double.parseDouble(args[3]);
        int maxIterCount = Integer.parseInt(args[4]);

        double[] result = diffEvoAlg.run(populationSize, random, crossoverConstant, maxIterCount, desiredError);

        System.out.println(Arrays.toString(result));
    }

}
