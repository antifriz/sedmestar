package hr.fer.zemris.optjava.dz6;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Random;

/**
 * Created by ivan on 11/15/15.
 */
class Ant implements Comparable<Ant>, Iterable<Integer> {
    double fitness;

    private LinkedHashSet<Integer> path = new LinkedHashSet<>();


    boolean tryAddNode(int nodeIdx) {
        return path.add(nodeIdx);
    }

    boolean hasVisited(int nodeIdx) {
        return path.contains(nodeIdx);
    }

    @Override
    public int compareTo(Ant o) {
        return Double.compare(o.fitness, fitness);
    }

    public static Ant withTraversal(Random random, double[][] pheromones, double[][] distances, int[][] candidatesList) {
        Ant ant = new Ant();
        ant.traverse(random, pheromones, distances, candidatesList);
        ant.updateFitness(distances);
        return ant;
    }

    private void traverse(Random random, double[][] pheromones, double[][] distances, int[][] candidatesList) {
        int numberOfNodes = pheromones.length;
        assert numberOfNodes == distances.length && numberOfNodes == candidatesList.length;

        int next = random.nextInt(distances.length);

        for (int i = 0; i < numberOfNodes - 1; i++) {
            int last = next;

            if (areVisitedAll(candidatesList[last])) {
                next = rouletteWheelPick(random, pheromones[last], candidatesList[last]);
            } else {
                next = rouletteWheelPick(random, pheromones[last]);
            }
        }
    }

    private int rouletteWheelPick(Random random, double[] pheromones, int[] nodes) {
        double sum = 0;
        for (int node : nodes) {
            sum += pheromones[node];
        }

        double rnd = random.nextDouble() * sum;

        for (int i = 0; i < nodes.length; i++) {
            rnd -= pheromones[nodes[i]];
            if (rnd < 0) {
                return i;
            }
        }
        throw new RuntimeException();
    }


    private int rouletteWheelPick(Random random, double[] pheromones) {
        double sum = 0;
        for (double pheromone : pheromones) {
            sum += pheromone;
        }

        double rnd = random.nextDouble() * sum;

        for (int i = 0; i < pheromones.length; i++) {
            rnd -= pheromones[i];
            if (rnd < 0) {
                return i;
            }
        }
        throw new RuntimeException();
    }

    private boolean areVisitedAll(int[] nodes) {
        for (int node : nodes) {
            if (!path.contains(node)) {
                return false;
            }
        }
        return true;
    }

    private void updateFitness(double[][] distances) {
        Iterator<Integer> it = iterator();
        int first = it.next();
        int next = first;
        double distance = 0;
        while (it.hasNext()) {
            int last = next;
            next = it.next();
            distance += distances[next][last];
        }
        distance += distances[first][next];

        fitness = 1 / distance;
    }

    public static Ant badOne() {
        Ant ant = new Ant();
        ant.fitness = Double.MIN_VALUE;
        return ant;
    }

    @Override
    public Iterator<Integer> iterator() {
        return path.iterator();
    }
}
