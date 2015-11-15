package hr.fer.zemris.optjava.dz6;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.IntStream;

public class Main {

    public static final double DEFAULT_ALPHA = 0.1;
    public static final double DEFAULT_BETA = 0.2;
    public static final double DEFAULT_A = 0.3;

    public static void main(String[] args) throws IOException {
        double alpha = DEFAULT_ALPHA;
        double beta = DEFAULT_BETA;
        double a = DEFAULT_A;

        run(args[0], Integer.valueOf(args[1]), Integer.valueOf(args[2]), Integer.valueOf(args[3]), alpha, beta, a);
    }


    public static Double run(String fileName, int candidateListSize, int colonySize, int maxIter, double alpha, double beta, double a) throws IOException {
        Double[][] euclidians = Files.lines(Paths.get(fileName)).filter(x -> x.matches("^[ 0-9].*")).map(String::trim).map(x -> x.split(" ")).map(x -> new Double[]{Double.valueOf(x[1]), Double.valueOf(x[2])}).toArray(Double[][]::new);

        assert candidateListSize < euclidians.length;


        int numberOfNodes = euclidians.length;
        final int[][] candidatesList = new int[numberOfNodes][candidateListSize];
        final double[][] distances = new double[numberOfNodes][numberOfNodes];

        for (int i = 0; i < numberOfNodes; i++) {
            final double x1 = euclidians[i][0];
            final double y1 = euclidians[i][1];
            for (int j = 0; j < numberOfNodes; j++) {
                distances[i][j] = Point2D.distance(x1, y1, euclidians[j][0], euclidians[j][1]);
            }
            final int idx = i;
            int[] order = IntStream.range(0, distances.length).mapToObj(x -> x).sorted((p, q) -> Double.compare(distances[idx][p], distances[idx][q])).filter(x -> x != idx).limit(candidateListSize).mapToInt(x -> x).toArray();
            assert order.length == candidateListSize;
            System.arraycopy(order, 0, candidatesList[i], 0, order.length);
        }
        //assert idx == i;
        //double[] candidates = Arrays.stream(euclidians).map(x -> new Object[]{x[0], Point2D.distance((Double) x[1], (Double) x[2], x1, y1)}).sorted((p, q) -> Double.compare((Double) q[1], (Double) p[1])).limit(candidateListSize + 1).filter(x -> (Integer) x[0] != idx).mapToDouble(x -> (Double) x[0]).toArray();
        //assert candidates.numberOfNodes == candidateListSize;
        //System.arraycopy(candidates, 0, candidatesList, 0, candidates.numberOfNodes);

        final double[][] pheromones = new double[numberOfNodes][numberOfNodes];

        double Tmax = 1;

        double Tmin = 0.1;

        for (double[] pheromone : pheromones) {
            Arrays.fill(pheromone, Tmax);
        }


        final Random random = new Random();

        Ant bestSoFar = Ant.badOne();
        for (int i = 0; i < maxIter; i++) {
            PriorityQueue<Ant> ants = new PriorityQueue<>();

            Ant best = IntStream.range(0, colonySize).mapToObj(x -> Ant.withTraversal(random, pheromones, distances, candidatesList)).max(Comparator.<Ant>naturalOrder()).get();
            if (best.compareTo(bestSoFar) == 1) {
                bestSoFar = best;
            }

            evaporate(pheromones, a);
            updatePheromones(pheromones, pickAnt(random, bestSoFar, best, 0.5));
        }
        return bestSoFar.fitness;

    }

    private static Ant pickAnt(Random random, Ant bestSoFar, Ant best, double factor) {
        return random.nextDouble() > factor ? best : bestSoFar;
    }

    private static void evaporate(double[][] pheromones, double a) {
        for (double[] d : pheromones) {
            int length = d.length;
            for (int i = 0; i < length; i++) {
                d[i] *= (1 - a);
            }
        }
    }

    private static void updatePheromones(double[][] pheromones, Ant ant) {
        Iterator<Integer> it = ant.iterator();
        int first = it.next();
        int next = first;
        while(it.hasNext()){
            int last = next;
            next = it.next();
            pheromones[last][next] = pheromones[next][last] += ant.fitness;
        }
        pheromones[next][first] = pheromones[first][next] +=ant.fitness;
    }

}
