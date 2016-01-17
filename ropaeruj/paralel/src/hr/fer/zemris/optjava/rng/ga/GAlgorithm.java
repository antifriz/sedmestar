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

        EventQueue.invokeLater(() -> {
            ex = new PointsFrame(image.getWidth(), image.getHeight());

            ex.setVisible(true);
        });

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
            }

            best = solutions.stream().max(Comparator.<RectSolution>reverseOrder()).get();
            if (i % (1000 / POP_SIZE) == 0) {

                long cur = System.currentTimeMillis();
                System.out.printf("[%7d] %7.0f %4dms\n", i, -best.fitness, cur - last);
                last = cur;
                GrayScaleImage bestImage = evaluator.draw(best, image);
                if (i % 10_000 == 0) {
                    if ((-best.fitness) < 500_000) {

                        bestImage.save(new File("box" + (((int) -best.fitness) / 10_000) + ".png"));
                    }
                }
                if (ex != null) {
                    ex.newData(bestImage);
                }
            }


            for (int j = 1; j < solutions.size(); j++) {

                RectSolution papa;
                RectSolution mama;
                List<RectSolution> kTournamentSolutions = new ArrayList<>();
                for (RectSolution solution : solutions) {
                    if (random.nextDouble() <= 0.4) {
                        kTournamentSolutions.add(solution);
                    }
                }
                if (kTournamentSolutions.size() <= 2) {
                    kTournamentSolutions.add(solutions.get(0));
                    kTournamentSolutions.add(best);
                }
                kTournamentSolutions.sort(Comparator.<RectSolution>naturalOrder());
                mama = kTournamentSolutions.get(0);
                papa = kTournamentSolutions.get(1);

                RectSolution child = (RectSolution) mama.duplicate();
                int[] papaData = papa.getData();
                int[] mamaData = mama.getData();
                int[] childData = child.getData();
                for (int k = 0; k < solutionSize; k++) {
                    boolean b = papaData[k] > mamaData[k];
                    int max = b ? papaData[k] : mamaData[k];
                    int min = !b ? papaData[k] : mamaData[k];
                    childData[k] = (int) Math.round(random.nextDouble() * (max - min) + min);
                    if (random.nextDouble() <= MUTATION_CHANCE) {
                        childData[k] = random.nextInt(0, limits[k]);
                    }
                }
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
}
