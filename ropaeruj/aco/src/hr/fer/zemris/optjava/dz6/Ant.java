package hr.fer.zemris.optjava.dz6;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Random;

/**
 * Created by ivan on 11/15/15.
 */
class Ant implements Comparable<Ant>, Iterable<Integer> {
    double fitness;
    double distance;

    private LinkedHashSet<Integer> path = new LinkedHashSet<>();


    @Override
    public int compareTo(Ant o) {
        return Double.compare(this.fitness, o.fitness);
    }

    public static Ant withTraversal(Random random, double[][] pheromones, double[][] closenesses, int[][] candidatesList, double alpha, double[][] distances) {
        Ant ant = new Ant();
        ant.traverse(random, pheromones, closenesses, candidatesList, alpha);
        ant.updateFitness(distances);
        return ant;
    }

    private void traverse(Random random, double[][] pheromones, double[][] closenesses, int[][] candidatesList, double alpha) {
        int numberOfNodes = pheromones.length;
        assert numberOfNodes == closenesses.length && numberOfNodes == candidatesList.length;

        int next = random.nextInt(numberOfNodes);
        path.add(next);

        for (int i = 0; i < numberOfNodes - 1; i++) {
            int last = next;

            if (!areVisitedAll(candidatesList[last])) {
                next = rouletteWheelPick(random, pheromones[last], closenesses[last], candidatesList[last], alpha);
            } else {
                next = rouletteWheelPick(random, pheromones[last], closenesses[last], alpha);
            }
            path.add(next);
        }
    }

    private int rouletteWheelPick(Random random, double[] pheromones, double[] closenesses, int[] nodes, double alpha) {
        double sum = 0;
        for (int node : nodes) {
            if (!path.contains(node)) {
                sum += Math.pow(pheromones[node], alpha) * closenesses[node];
            }
        }

        double rnd = random.nextDouble() * sum;

        for (int node : nodes) {
            if (!path.contains(node)) {
                rnd -= Math.pow(pheromones[node], alpha) * closenesses[node];
                if (rnd < 0) {
                    return node;
                }
            }
        }
        throw new RuntimeException();
    }


    private int rouletteWheelPick(Random random, double[] pheromones, double[] closenesses, double alpha) {
        double sum = 0;
        for (int i = 0; i < pheromones.length; i++) {
            if (!path.contains(i)) {
                double pheromone = Math.pow(pheromones[i], alpha) * closenesses[i];
                sum += pheromone;
            }
        }

        double rnd = random.nextDouble() * sum;

        for (int i = 0; i < pheromones.length; i++) {
            if (!path.contains(i)) {
                rnd -= Math.pow(pheromones[i], alpha) * closenesses[i];
                if (rnd < 0) {
                    return i;
                }
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
        this.distance = distance;
    }

    public static Ant badOne() {
        Ant ant = new Ant();
        ant.fitness = Double.MIN_VALUE;
        return ant;
    }

    @Override
    public String toString() {
        int[] nodes = path.stream().mapToInt(x -> x).toArray();

        int idx = 0;
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] == 0) {
                idx = i;
                break;
            }
        }

        int[] nodesNormalized = new int[nodes.length];
        System.arraycopy(nodes, idx, nodesNormalized, 0, nodes.length - idx);
        if (idx > 0) {
            System.arraycopy(nodes, 0, nodesNormalized, nodes.length - idx, idx);
        }
        for (int i = 0; i < nodesNormalized.length; i++) {
            nodesNormalized[i]++;
        }
        return Arrays.toString(nodesNormalized);
    }

    @Override
    public Iterator<Integer> iterator() {
        return path.iterator();
    }

}
