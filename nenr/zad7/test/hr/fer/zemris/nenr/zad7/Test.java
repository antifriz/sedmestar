package hr.fer.zemris.nenr.zad7;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by ivan on 1/23/16.
 */
public class Test {
    @org.junit.Test
    public void test283() throws Exception {
        test(new int[]{2, 8, 3});
    }

    @org.junit.Test
    public void test2843() throws Exception {
        test(new int[]{2, 8, 4, 3});
    }

    @org.junit.Test
    public void test2643() throws Exception {
        test(new int[]{2, 6, 4, 3});
    }

    private void test(int[] layers) throws IOException {
        IReadOnlyDataset dataset = ParseableReadOnlyDataset.createFromFile("data.txt");

        FunkyNeuralNetwork funkyNeuralNetwork = new FunkyNeuralNetwork(layers);


        GATrainer trainer = new GATrainer(funkyNeuralNetwork, dataset, 10, 1000000, 0.9, 0.1, 0.2, 0.02);
        trainer.trainFFANN();
    }

    @org.junit.Test
    public void testT() throws Exception {
        IReadOnlyDataset dataset = ParseableReadOnlyDataset.createFromFile("data.txt");
        double[] vec = new double[]{0.95,1.0,0.001,0.01};
        int vecVal = calcFor(vec, dataset);
        Random random = new Random();
        while(true){
            double [] vecR = Arrays.copyOf(vec,vec.length);
            for (int i = 0; i < vecR.length; i++) {
                vecR[i] *=random.nextDouble()*0.02 + 0.98;
            }
            int vecRVal = calcFor(vecR,dataset);
            if(vecRVal<vecVal){
                vecVal = vecRVal;
                vec = vecR;
            }
        }


    }

    private int calcFor(double[] vec, IReadOnlyDataset dataset){
        double v1 = vec[0];
        double s1 = vec[1];
        double s2 = vec[2];
        double mp = vec[3];
        int s = Arrays.stream(new int[][]{ {2, 8, 4, 3}, {2, 6, 4, 3}}).parallel()
                .mapToInt(layers -> new GATrainer(new FunkyNeuralNetwork(layers), dataset, 10, 100000, v1, s1, s2, mp).trainFFANN()).sum();
        System.out.printf("%10d | %f %f %f %f\n",s,v1,s1,s2,mp);
        return s;

    }
}
