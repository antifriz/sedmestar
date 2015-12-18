package hr.fer.zemris.ropaeruj.nsga;


import java.util.*;

/**
 * Created by ivan on 10/31/15.
 */
public class MOOP {

    public static final int DIM = 6;

    public static void main(String[] args) {

        ArgsParser argsParser = new ArgsParser(args);

        Random random = new Random();

        MOOPProblem problem = new MOOPProblem() {
            @Override
            public int getNumberOfObjectives() {
                return 4;
            }

            @Override
            public void evaluateSolution(double[] solution, double[] objectives) {
                for (int i = 0; i < getNumberOfObjectives(); i++) {
                    objectives[i] = solution[i] * solution[i];
                }
            }

            @Override
            public double[] getSolutionMaxs() {
                return new double[]{5, 5, 5, 5};
            }

            @Override
            public double[] getSolutionMins() {
                return new double[]{-5, -5, -5, -5};
            }
        };

        double[] lower = problem.getSolutionMins();
        double[] upper = problem.getSolutionMaxs();

        Chromosome[] currentPopulation = new Chromosome[argsParser.getPopulationCount()];
        for (int i = 0; i < currentPopulation.length; i++) {
            currentPopulation[i] = new Chromosome(DIM);
            currentPopulation[i].randomize(random, lower, upper);
        }
        Chromosome[] nextPopulation = new Chromosome[currentPopulation.length];

        geneticAlgorithmLoop(argsParser, random, currentPopulation, nextPopulation, problem);

    }

    private static void geneticAlgorithmLoop(ArgsParser argsParser, Random random, Chromosome[] currentPopulation, Chromosome[] nextPopulation, MOOPProblem problem) {
        int iterCount = 0;
        while (true) {

            print(currentPopulation, iterCount);

            iterCount++;

            if (isFinished(iterCount, currentPopulation, argsParser)) {
                System.out.printf("Ended with %d iterations, error %f -> %s\n", iterCount - 1, currentPopulation[0].fitness, currentPopulation[0]);
                return;
            }

            calculateFitness(
                    currentPopulation,
                    problem,
                    argsParser.getShareSigma(),
                    argsParser.getEpsilon(),
                    argsParser.useSolutionSpaceDensity()
            );

            nextPopulationUsingRouletteWheelSelection(iterCount, argsParser, random, currentPopulation, nextPopulation);

            Chromosome[] k = currentPopulation;
            currentPopulation = nextPopulation;
            nextPopulation = k;

            //evaluate(function, currentPopulation);
        }
    }


    private static void calculateFitness(Chromosome[] currentPopulation, MOOPProblem problem, double sigmaShare, double epsilon, boolean isSolutionSpaceDensity) {
        int N = currentPopulation.length;
        double fmin = N + epsilon;
        List<List<Chromosome>> undominantlySortedPopulation = undominantlySort(currentPopulation, problem);
        for (List<Chromosome> front : undominantlySortedPopulation) {
            double[][] frontValues;
            frontValues = isSolutionSpaceDensity ? front.stream().map(chromosome -> chromosome.values).toArray(double[][]::new) : front.stream().map(chromosome -> chromosome.evaluation).toArray(double[][]::new);
            double fminNew = fmin;
            for (Chromosome q : front) {
                q.fitness = fmin - epsilon;
                q.fitness /= density(isSolutionSpaceDensity ? q.values : q.evaluation, frontValues, sigmaShare);
                if (q.fitness < fminNew) {
                    fminNew = q.fitness;
                }
            }

            fmin = fminNew;
        }


    }

    private static double density(double[] q, double[][] arrays, double sigmaShare) {
        int dim = q.length;
        double[] maxs = Arrays.copyOf(q, dim);
        double[] mins = Arrays.copyOf(q, dim);
        double d = 0;
        for (double[] array : arrays) {
            for (int i = 0; i < dim; i++) {
                if (array[i] > maxs[i]) {
                    maxs[i] = array[i];
                } else if (array[i] < mins[i]) {
                    mins[i] = array[i];
                }
            }
        }
        for (double[] array : arrays) {
            double dd = 0;
            for (int j = 0; j < dim; j++) {
                double diff = q[j] - array[j];

                diff /= maxs[j] - mins[j];
                diff *= diff;
                dd += diff;
            }
            dd = Math.sqrt(dd);
            d += sigmaShare >= dd ? 1 - (dd / sigmaShare) * (dd / sigmaShare) : 0;
        }
        return d;
    }

    private static List<List<Chromosome>> undominantlySort(Chromosome[] currentPopulation, MOOPProblem problem) {
        int N = currentPopulation.length;
        List<List<Chromosome>> fronts = new ArrayList<>();
        int k = 0;
        for (Chromosome chromosomeI : currentPopulation) {
            chromosomeI.eta = 0;
            chromosomeI.S = Collections.emptyList();
            for (Chromosome chromosomeJ : currentPopulation) {
                if (chromosomeI == chromosomeJ) {
                    continue;
                }
                if (isDominant(chromosomeI, chromosomeJ, problem)) {
                    chromosomeI.S.add(chromosomeJ);
                } else if (isDominant(chromosomeJ, chromosomeI, problem)) {
                    chromosomeJ.S.add(chromosomeI);
                    chromosomeI.eta++;
                }
            }
            if (chromosomeI.eta == 0) {
                fronts.get(0).add(chromosomeI);
                k = 1;
            }
        }

        while (!fronts.get(k).isEmpty()) {
            List<Chromosome> Q = new ArrayList<>();
            for (int i = 0; i < fronts.get(k).size(); i++) {
                Chromosome frontChromosome = fronts.get(k).get(i);
                for (int j = 0; j < frontChromosome.S.size(); j++) {
                    Chromosome c = frontChromosome.S.get(j);
                    c.eta--;
                    if (c.eta == 0) {
                        Q.add(c);
                    }
                }
            }
            k++;
            fronts.set(k, Q);
        }
        return fronts;
    }

    private static boolean isDominant(Chromosome a, Chromosome b, MOOPProblem problem) {
        int numberOfObjectives = problem.getNumberOfObjectives();
        if (a.evaluation == null) {
            a.evaluation = new double[numberOfObjectives];
            problem.evaluateSolution(a.values, a.evaluation);
        }
        if (b.evaluation == null) {
            b.evaluation = new double[numberOfObjectives];
            problem.evaluateSolution(b.values, b.evaluation);
        }
        int cnt = 0;
        for (int i = 0; i < numberOfObjectives; i++) {
            if (a.evaluation[i] > b.evaluation[i]) {
                return false;
            }
            if (a.evaluation[i] < b.evaluation[i]) {
                cnt++;
            }

        }
        return cnt > 0;
    }

    private static void nextPopulationUsingRouletteWheelSelection(int iterCount, ArgsParser argsParser, Random random, Chromosome[] currentPopulation, Chromosome[] nextPopulation) {
        for (int i = 1; i < nextPopulation.length; i++) {
            Chromosome mama = rouletteWheelSelection(currentPopulation, random);
            Chromosome papa = rouletteWheelSelection(currentPopulation, random);

            tweakChild(iterCount, argsParser, random, nextPopulation, i, mama, papa);
        }
    }

    private static void tweakChild(int iterCount, ArgsParser argsParser, Random random, Chromosome[] nextPopulation, int i, Chromosome mama, Chromosome papa) {
        Chromosome child = mama.newLikeThis();
        for (int j = 0; j < child.values.length; j++) {
            double min = Math.min(mama.values[j], papa.values[j]);
            double max = Math.max(mama.values[j], papa.values[j]);
            child.values[j] = min + (max - min) * random.nextDouble() + random.nextGaussian() * argsParser.getMutationSigma() * Math.pow(1 - iterCount / (double) argsParser.getMaxIterCount(), 10);
        }
        nextPopulation[i] = child;
    }

    private static boolean isFinished(int iterCount, Chromosome[] currentPopulation, ArgsParser argsParser) {
        return iterCount > argsParser.getMaxIterCount() || -currentPopulation[0].fitness < argsParser.getDesiredError();
    }

    private static void print(Chromosome[] currentPopulation, int iterCount) {
        System.out.printf("[%6d]", iterCount);
        System.out.printf("%s %f ", currentPopulation[0], currentPopulation[0].fitness);
        System.out.println();
    }

    private static Chromosome rouletteWheelSelection(Chromosome[] array, Random random) {
        double minimal = Double.MAX_VALUE;
        double sum = 0;
        for (Chromosome anArray : array) {
            sum += anArray.fitness;
            minimal = Math.min(minimal, anArray.fitness);
        }
        sum -= minimal * array.length;

        double roulletePick = random.nextDouble() * sum;

        double it = 0;
        for (Chromosome a : array) {
            it += a.fitness - minimal;
            if (it > roulletePick) {
                return a;
            }
        }
        return array[random.nextInt(array.length)];
    }

}
