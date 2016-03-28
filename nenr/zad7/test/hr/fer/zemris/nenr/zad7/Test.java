package hr.fer.zemris.nenr.zad7;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.IntUnaryOperator;
import java.util.stream.IntStream;

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

    @org.junit.Test
    public void randomTest() throws Exception{
        for (int i = 0; i < 100; i++) {

        final int d1 = ThreadLocalRandom.current().nextInt(100)+1;
        final int d2 = ThreadLocalRandom.current().nextInt(d1)+1;
        final int[] d1a = IntStream.range(0, d1).toArray();

        String message = "" + d1 + " " + d2;
        System.out.println(message);
            IntUnaryOperator intUnaryOperator = x -> (int) ( x / (float) d2 * d1 + 0.5);
            String message1 = Arrays.toString(IntStream.range(0, d2).map(intUnaryOperator).toArray());
            System.out.println(message1);
            org.junit.Assert.assertEquals(message1, (IntStream.range(0, d2).map(intUnaryOperator).reduce((a, b) -> a == b ? 1 : 0).getAsInt()), 0);
        }
    }

    private void test(int[] layers) throws IOException {
        IReadOnlyDataset dataset = ParseableReadOnlyDataset.createFromFile("data.txt");

        FunkyNeuralNetwork funkyNeuralNetwork = new FunkyNeuralNetwork(layers);


        GATrainer trainer = new GATrainer(funkyNeuralNetwork, dataset, 10, 1000000, 0.99, 0.1, 0.2, 0.04);
        trainer.trainFFANN();
    }

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
