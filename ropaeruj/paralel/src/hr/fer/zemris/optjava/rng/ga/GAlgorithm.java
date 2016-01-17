package hr.fer.zemris.optjava.rng.ga;

import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.RNG;
import hr.fer.zemris.optjava.rng.image.GrayScaleImage;
import hr.fer.zemris.optjava.rng.preview.PointsFrame;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by ivan on 1/16/16.
 */
public class GAlgorithm {

    private static final int MAX_ITER_COUNT = 100_000_000;
    private static final int POP_SIZE = 10;
    private static final int RECT_COUNT = 200;
    public static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors() - 1;
    public static final double MUTATION_CHANCE = 0.001;
    public static final String IMAGE_PATH = "kuca.png";


    private static PointsFrame ex;

    public static void main(String[] args) throws IOException, InterruptedException {


        File imageFile = new File(IMAGE_PATH);
        final BlockingQueue<RectSolution> requestQueue = new LinkedBlockingQueue<>();
        final BlockingQueue<RectSolution> responseQueue = new LinkedBlockingQueue<>();
        IntStream.range(0, THREAD_COUNT)
                .mapToObj(x -> new RectEvoThread(requestQueue, responseQueue, imageFile))
                .forEach(Thread::start);

        GrayScaleImage image = GrayScaleImage.load(imageFile);

//
//        EventQueue.invokeLater(() -> {
//            ex = new PointsFrame(image.getWidth(), image.getHeight());
//
//            ex.setVisible(true);
//        });

        Evaluator evaluator = new Evaluator(image);

        IRNG random = RNG.getRNG();
        List<RectSolution> solutions = IntStream.range(0, POP_SIZE).mapToObj(i -> new RectSolution(RECT_COUNT, image.getWidth(), image.getHeight())).collect(Collectors.toList());
        int solutionSize = solutions.get(0).data.length;
        int[] limits = solutions.get(0).limits;

        for (RectSolution solution : solutions) {
            requestQueue.put(solution);
        }
        long last = System.currentTimeMillis();
        RectSolution best = solutions.get(0);
        for (int i = 0; i < MAX_ITER_COUNT; i++) {
            solutions.clear();
            solutions.add(best);
            for (int j = 0; j < POP_SIZE - 1; j++) {
                solutions.add(responseQueue.take());
//                System.out.println("primio response");
            }

            best = solutions.stream().max(Comparator.<RectSolution>reverseOrder()).get();//solutions.stream().max(Comparator.<RectSolution>naturalOrder()).get();
            if (i % (10000 / POP_SIZE) == 0) {

                long cur = System.currentTimeMillis();
                System.out.printf("[%7d] %f %d\n", i, -best.fitness, cur - last);
                last = cur;
                if (i % 10_000 == 0) {
                    GrayScaleImage bestImage = evaluator.draw(best, image);
                    if ((-best.fitness) < 500_000) {

                        bestImage.save(new File("box" + (((int) -best.fitness) / 10_000) + ".png"));
                    }
                }
//                if (ex != null) {
//                    ex.newData(bestImage);
//                }

            }


            for (int j = 1; j < solutions.size(); j++) {

                RectSolution papa;
                RectSolution mama;
//                if ((j % 2) == 0) {
                List<RectSolution> kTournamentSolutions = new ArrayList<>();
                //kTournamentSolutions.add(best);
                for (RectSolution solution:solutions) {
                    if(random.nextDouble()<=0.4) {
                        kTournamentSolutions.add(solution);
                    }
                }
                if(kTournamentSolutions.size()<=2){
                    kTournamentSolutions.add(solutions.get(0));
                    kTournamentSolutions.add(best);
                }
                kTournamentSolutions.sort(Comparator.<RectSolution>naturalOrder());
                mama = kTournamentSolutions.get(0);
                papa = kTournamentSolutions.get(1);
//                } else {
//                    mama = rouletteWheelSelection(solutions, random);
//                    do {
//                        papa = rouletteWheelSelection(solutions, random);
//                    } while (mama == papa);
//                }
//                RectSolution mama = rouletteWheelSelection(solutions, random);
//                RectSolution papa;

                RectSolution child = (RectSolution) mama.duplicate();
                int[] papaData = papa.getData();
                int[] mamaData = mama.getData();
                int[] childData = child.getData();
                for (int k = 0; k < solutionSize; k++) {
                    boolean b = papaData[k] > mamaData[k];
                    int max = b ? papaData[k] : mamaData[k];
                    int min = !b ? papaData[k] : mamaData[k];
                    childData[k] = (int) Math.round(random.nextDouble() * (max - min) + min);
//                    childData[k] +=random.nextGaussian();
                    if (random.nextDouble() <= MUTATION_CHANCE) {
//                        if ((j % 2) == 0) {
                        childData[k] = random.nextInt(0, limits[k]);
//                        } else {
//                            childData[k] += random.nextInt(-10, 11);
//                            childData[k] = Math.min(Math.max(0, childData[k]), limits[k]);
//                        }
                    }
                }
//                for (int r = 1; r < solutionSize; r+=5) {
//                    if (random.nextBoolean()) {
//                        System.arraycopy(papaData, r, childData, r, 5);
////                        int secondRect = random.nextInt(0, RECT_COUNT);
////                        for (int k = 0; k < 5; k++) {
////                            int sw = childData[r + k];
////                            childData[r + k] = childData[secondRect + k];
////                            childData[secondRect + k] = sw;
////                        }
//                    }
//                }
//                for (int r = 1; r < solutionSize; r += 5) {
//
//                    if (random.nextDouble() <= 1.0 / RECT_COUNT) {
//                        for (int k = 0; k < 5; k++) {
//
//                            childData[r + k] = random.nextInt(0, limits[r + k]);
////                            childData[r + k] += random.nextGaussian();
//                            childData[r + k] = Math.min(Math.max(0, childData[r + k]), limits[r + k]);
//                        }
////                        int secondRect = random.nextInt(0, RECT_COUNT);
////                        for (int k = 0; k < 5; k++) {
////                            int sw = childData[r + k];
////                            childData[r + k] = childData[secondRect + k];
////                            childData[secondRect + k] = sw;
////                        }
//                    }
//                }
//
//
//                for (int r = 1; r < solutionSize; r += 5) {
//                    int diff1 = childData[r] + childData[r + 2] - limits[r];
//                    if (diff1 > 0) {
//                        childData[r + 2] = random.nextInt(0, limits[r] - diff1);
//                    }
//                    int diff2 = childData[r + 1] + childData[r + 3] - limits[r + 1];
//
//                    if (diff2 > 0) {
//                        childData[r + 3] = random.nextInt(0, limits[r + 1] -  diff2);
//                    }
//                }


//                if (random.nextBoolean()) {
//                    if (random.nextBoolean()) {
//                        childData[0] = papaData[0];
//                    }
//                    for (int r = 1; r < solutionSize; r += 5) {
//                        if (random.nextBoolean()) {
//                            System.arraycopy(papaData, r, childData, r, 5);
//                        }
//                    }
//                } else {
//                    for (int k = 0; k < solutionSize; k++) {
//                        if (random.nextDouble() <= MUTATION_CHANCE) {
//                            if ((j % 2) == 0) {
//                                childData[k] = random.nextInt(0, limits[k]);
//                            } else {
//                                childData[k] += random.nextInt(-10, 11);
//                                childData[k] = Math.min(Math.max(0, childData[k]), limits[k]);
//                            }
//                        }
//                    }
//                }
//
/*
                while (random.nextDouble() <= MUTATION_CHANCE) {
                    int firstRect = random.nextInt(0, RECT_COUNT);
                    for (int k = 0; k < 5; k++) {
                        childData[firstRect + k] = random.nextInt(0, limits[firstRect + k]);
                    }
                }

                while (random.nextDouble() <= MUTATION_CHANCE) {
                    int k = random.nextInt(0, 5);
                    int firstRect = random.nextInt(0, RECT_COUNT);
                    int secondRect = random.nextInt(0, RECT_COUNT);
                    int sw = childData[firstRect + k];
                    childData[firstRect + k] = childData[secondRect + k];
                    childData[secondRect + k] = sw;
                }*/
                    int rr = random.nextInt(-3, 3);
                    for (int r = 0; r < rr; r++) {

                        int firstRect = random.nextInt(0, RECT_COUNT) * 5 + 1;
                        int secondRect = random.nextInt(0, RECT_COUNT) * 5 + 1;
                        for (int k = 0; k < 5; k++) {
                            int sw = childData[firstRect + k];
                            childData[firstRect + k] = childData[secondRect + k];
                            childData[secondRect + k] = sw;
                        }
                    }
                requestQueue.put(child);
            }

        }
    }


    private static RectSolution rouletteWheelSelection(List<RectSolution> solutions, IRNG random) {
        double minimal = Double.MAX_VALUE;
        double sum = 0;
        for (RectSolution anArray : solutions) {
            sum += anArray.fitness;
            minimal = Math.min(minimal, anArray.fitness);
        }
        sum -= minimal * (solutions.size() + 1);
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
