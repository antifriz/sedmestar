package hr.fer.zemris.optjava.dz6;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * preporuceni parametri data/ch150.tsp 5 200 1000
 *
 * cini mi se kako imam bug u ukupnoj udaljenosti, ali ciklus je ispravan (kao sto se moze vidjeti na GUI-ju)
 */

public class Main {

    public static final double DEFAULT_ALPHA = 1;
    public static final double DEFAULT_BETA = 1;
    public static final double DEFAULT_A = 5000;

    static PointsFrame ex;

    public static void main(String[] args) throws IOException {
        double alpha = DEFAULT_ALPHA;
        double beta = DEFAULT_BETA;
        double a = DEFAULT_A;
        EventQueue.invokeLater(() -> {
            ex = new PointsFrame();

            ex.setVisible(true);
        });

        java.util.List<String> strings = Files.readAllLines(Paths.get("data/bookmarks.kml")).stream().map(String::trim).filter(x -> x.trim().startsWith("<coordinates>")).map(x -> x.substring(x.indexOf(">") + 1, x.indexOf("</")).replaceAll(","," ")).filter(x->{
            String[] s = x.split(" ");
            return Double.valueOf(s[0]) >=16.33161103 && Double.valueOf(s[0])<16.4 && Double.valueOf(s[1])<48.211864;
        }).collect(Collectors.toList());
        java.util.List<String> collect = IntStream.range(0, strings.size()).mapToObj(i -> i + " " + strings.get(i)).collect(Collectors.toList());

        String tspFile = "data/bookmarks.tsp";
        Files.write(Paths.get(tspFile), collect, Charset.defaultCharset());

        run(args[0], Integer.valueOf(args[1]), Integer.valueOf(args[2]), Integer.valueOf(args[3]), alpha, beta, a);
    }


    public static Double run(String fileName, int candidateListSize, int colonySize, int maxIter, double alpha, double beta, double a) throws IOException {
        System.out.println("Trying " + fileName);

        Double[][] euclidians = Files.lines(Paths.get(fileName)).filter(x -> x.matches("^[ 0-9].*")).map(String::trim).map(x -> x.replaceAll(" +", " ").split(" ")).map(x -> new Double[]{Double.valueOf(x[1]), Double.valueOf(x[2])}).toArray(Double[][]::new);

        assert candidateListSize < euclidians.length;


        int numberOfNodes = euclidians.length;
        final int[][] candidatesList = new int[numberOfNodes][candidateListSize];
        final double[][] closeness = new double[numberOfNodes][numberOfNodes];
        final double[][] distances = new double[numberOfNodes][numberOfNodes];

        for (int i = 0; i < numberOfNodes; i++) {
            final double x1 = euclidians[i][0];
            final double y1 = euclidians[i][1];
            for (int j = 0; j < numberOfNodes; j++) {
                distances[i][j] = Point2D.distance(x1, y1, euclidians[j][0], euclidians[j][1]);
                closeness[i][j] = 1 / Math.pow(distances[i][j], beta);
            }
            final int idx = i;
            int[] order = IntStream.range(0, distances.length).mapToObj(x -> x).sorted((p, q) -> Double.compare(distances[idx][p], distances[idx][q])).filter(x -> x != idx).limit(candidateListSize).mapToInt(x -> x).toArray();
            assert order.length == candidateListSize;
            System.arraycopy(order, 0, candidatesList[i], 0, order.length);
        }

        final double[][] pheromones = new double[numberOfNodes][numberOfNodes];
        final Random random = new Random();

        double ro = 0.12;

        initializePheromones(pheromones, 1);


        // dovoljno dobro
        double Tmax = 1 / ro / Ant.withTraversal(random, pheromones, closeness, candidatesList, alpha, distances).distance;

        double Tmin = Tmax / a;

        initializePheromones(pheromones, Tmax);


        double T = Tmax;

        Ant bestSoFar = Ant.badOne();
        for (int i = 0; i < maxIter; i++) {
            Ant best = IntStream.range(0, colonySize).parallel().mapToObj(x -> Ant.withTraversal(random, pheromones, closeness, candidatesList, alpha, distances)).max(Comparator.<Ant>naturalOrder()).get();
            if (best.compareTo(bestSoFar) == 1) {
                bestSoFar = best;
            }
            T *= (1 - ro);
            if (T < Tmin) {
                Tmax = 1 / ro / bestSoFar.distance;
                initializePheromones(pheromones, Tmax);
                T = Tmax;
            }
            if (ex != null) {
                ex.newData(bestSoFar, euclidians);
            }
            System.out.printf("[%5d] Best so far %6.4f %6.0f %s\n", i, T, bestSoFar.distance, bestSoFar);

            evaporate(pheromones, ro);
            updatePheromones(pheromones, pickAnt(random, bestSoFar, best, 1 - i / (double) maxIter));
        }
        return bestSoFar.distance;

    }


    private static void initializePheromones(double[][] pheromones, double tmax) {
        for (double[] pheromone : pheromones) {
            Arrays.fill(pheromone, tmax);
        }
    }

    private static Ant pickAnt(Random random, Ant bestSoFar, Ant best, double factor) {
        return random.nextDouble() > factor ? best : bestSoFar;
    }

    private static void evaporate(double[][] pheromones, double ro) {
        for (double[] d : pheromones) {
            int length = d.length;
            for (int i = 0; i < length; i++) {
                d[i] *= (1 - ro);
            }
        }
    }

    private static void updatePheromones(double[][] pheromones, Ant ant) {
        Iterator<Integer> it = ant.iterator();
        int first = it.next();
        int next = first;
        while (it.hasNext()) {
            int last = next;
            next = it.next();
            pheromones[last][next] = pheromones[next][last] += ant.fitness;
        }
        pheromones[next][first] = pheromones[first][next] += ant.fitness;
    }

}
