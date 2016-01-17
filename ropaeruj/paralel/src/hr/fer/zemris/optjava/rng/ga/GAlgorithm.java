package hr.fer.zemris.optjava.rng.ga;

import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.RNG;
import hr.fer.zemris.optjava.rng.image.GrayScaleImage;
import hr.fer.zemris.optjava.rng.preview.PointsFrame;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by ivan on 1/16/16.
 */
public class GAlgorithm {

    private static final int MAX_ITER_COUNT = 10000000;
    private static final int POP_SIZE = 100;
    private static final int RECT_COUNT = 200;
    public static final int THREAD_COUNT = 4;//Runtime.getRuntime().availableProcessors();
    public static final double MUTATION_CHANCE = 0.0001;
    private static PointsFrame ex;

    public static void main(String[] args) throws IOException, InterruptedException {


        File imageFile = new File("kuca.png");
        final BlockingQueue<RectSolution> requestQueue = new LinkedBlockingQueue<>();
        final BlockingQueue<RectSolution> responseQueue = new LinkedBlockingQueue<>();
        IntStream.range(0, THREAD_COUNT)
                .mapToObj(x -> new RectEvoThread(requestQueue, responseQueue, imageFile))
                .forEach(Thread::start);

        GrayScaleImage image = GrayScaleImage.load(imageFile);


        EventQueue.invokeLater(() -> {
            ex = new PointsFrame(image.getWidth(),image.getHeight());

            ex.setVisible(true);
        });

        Evaluator evaluator = new Evaluator(image);

        IRNG random = RNG.getRNG();
        List<RectSolution> solutions = IntStream.range(0, POP_SIZE).mapToObj(i -> new RectSolution(RECT_COUNT, image.getWidth(), image.getHeight())).collect(Collectors.toList());
        int solutionSize = solutions.get(0).data.length;
        int[] limits = solutions.get(0).limits;
        List<RectSolution> newSolutions = new ArrayList<>();

        for (RectSolution solution : solutions) {
            requestQueue.put(solution);
        }


        for (int i = 0; i < MAX_ITER_COUNT; i++) {
            solutions.clear();
            for (int j = 0; j < POP_SIZE; j++) {
                solutions.add(responseQueue.take());
            }

            solutions.sort(Comparator.<RectSolution>naturalOrder());

            RectSolution best = solutions.get(0);//solutions.stream().max(Comparator.<RectSolution>naturalOrder()).get();

            System.out.printf("[%7d] %f\n", i, -best.fitness);



            newSolutions.clear();
            newSolutions.add(best);
            for (int j = 1; j < solutions.size(); j++) {
//                for (int k = 0; k < 3; k++) {
//                    RectSolution solution;
//                    do {
//                        solution = solutions.get(random.nextInt(0, solutions.size()));
//                    }while(kTournamentSolutions.contains(solution));
//                    kTournamentSolutions.add(solution);
//                }
//                kTournamentSolutions.sort(Comparator.<RectSolution>naturalOrder());
//                RectSolution mama = kTournamentSolutions.get(0);
//                RectSolution papa = kTournamentSolutions.get(1);

                RectSolution mama = rouletteWheelSelection(solutions,random);
                RectSolution papa;
                do {
                    papa = rouletteWheelSelection(solutions,random);
                } while (mama == papa);
                RectSolution child = (RectSolution) mama.duplicate();
                int[] papaData = papa.getData();
                int[] mamaData = mama.getData();
                int[] childData = child.getData();
                for (int k = 0; k < solutionSize; k++) {
                    boolean b= papaData[k] > mamaData[k];
                    int max = b?papaData[k]:mamaData[k];
                    int min = !b?papaData[k]:mamaData[k];
                    childData[k] = (int)Math.round(random.nextDouble() * (max - min) + min);
//                    childData[k] =random.nextBoolean()?papaData[k]:mamaData[k];
                    if(random.nextDouble()<=MUTATION_CHANCE){
                        childData[k]=random.nextInt(0,limits[k]);
                    }
                    childData[k] = Math.min(Math.max(0,childData[k]),limits[k]);
                }
//                System.arraycopy(papaData, 0, childData, 0, breakingPoint);
//
//                for (int k = 0; k < RECT_COUNT + 1; k+=5) {
//                    if(random.nextDouble()<=MUTATION_CHANCE){
//                        childData[k] += random.nextInt(0,255);
//                    }
//                }
//                for (int k = 0; k < solutionSize; k++) {
//                    if (random.nextDouble() <= MUTATION_CHANCE) {
//                        childData[k] += random.nextInt(0,limits[k]);
//                    }
//                }
//                while(random.nextDouble()<=0.1){
//                    int idx = random.nextInt(0,RECT_COUNT);
//                    for (int k = 0; k < 5; k++) {
//                        childData[idx+k] = random.nextInt(0,limits[idx+k]);
//                    }
//                }
                while(random.nextDouble()<=0.7){

                    int firstRect = random.nextInt(0,RECT_COUNT);
                    int secondRect =  random.nextInt(0,RECT_COUNT);
                    for (int k = 0; k < 5; k++) {
                        int sw = childData[firstRect+k];
                        childData[firstRect+k] = childData[secondRect+k];
                        childData[secondRect+k] = sw;
                    }
                }
                requestQueue.put(child);
            }
/*            if(i%100==0){
                GrayScaleImage bestImage = evaluator.draw(best, image);
//                bestImage.save(new File("box.png"));
                if (ex != null) {
                    ex.newData(bestImage);
                }

            }*/
        }
    }


    private static RectSolution rouletteWheelSelection(List<RectSolution> solutions, IRNG random) {
        double minimal = Double.MAX_VALUE;
        double sum = 0;
        for (RectSolution anArray : solutions) {
            sum += anArray.fitness;
            minimal = Math.min(minimal, anArray.fitness);
        }
        sum -= minimal * solutions.size();
        double roulletePick = random.nextDouble() * sum;

        double it = 0;
        for (RectSolution a : solutions) {
            it += a.fitness - minimal;
            if (it > roulletePick) {
                return a;
            }
        }
        return solutions.get(random.nextInt(0, solutions.size()));
    }

}
