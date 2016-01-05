package hr.fer.zemris.ropaeruj.nsga2;


import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

/**
 * Created by ivan on 10/31/15.
 * Preporucam parametre: 2 1000 100
 */
public class MOOP {
    static PointsFrame ex;

    public static void main(String[] args) throws IOException, InterruptedException {

        ArgsParser argsParser = new ArgsParser(args);

        Random random = new Random(69);

        MOOPProblem problem;
        if (argsParser.getProblemIdx() == 1) {
            problem = new MOOPProblem() {
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
        } else {
            problem = new MOOPProblem() {
                @Override
                public int getNumberOfObjectives() {
                    return 2;
                }

                @Override
                public void evaluateSolution(double[] solution, double[] objectives) {

                    objectives[0] = solution[0];
                    objectives[1] = (1 + solution[1]) / solution[0];
                }

                @Override
                public double[] getSolutionMaxs() {
                    return new double[]{1, 5};
                }

                @Override
                public double[] getSolutionMins() {
                    return new double[]{0.1, 0};
                }
            };

            EventQueue.invokeLater(() -> {
                ex = new PointsFrame();

                ex.setVisible(true);
            });
        }


        double[] lower = problem.getSolutionMins();
        double[] upper = problem.getSolutionMaxs();

        List<Chromosome> currentPopulation = new ArrayList<>();
        for (int i = 0; i < argsParser.getPopulationCount(); i++) {
            Chromosome chromosome = new Chromosome(problem.getDimension());
            chromosome.randomize(random, lower, upper);
            currentPopulation.add(chromosome);
        }
        List<Chromosome> nextPopulation = new ArrayList<>();

        geneticAlgorithmLoop(argsParser, random, currentPopulation, nextPopulation, problem);

        if (ex != null) {
            while (true) ;
        }
    }

    private static void geneticAlgorithmLoop(ArgsParser argsParser, Random random, List<Chromosome> currentPopulation, List<Chromosome> nextPopulation, MOOPProblem problem) throws IOException {
        int iterCount = 0;
        while (true) {
            System.out.println(++iterCount);

            calculateFitness(
                    currentPopulation,
                    argsParser.getPopulationCount(),
                    problem
            );

            if (isFinished(iterCount, argsParser)) {
                System.out.printf("Ended with %d iterations\nSolutions per bucket: %s", iterCount, Arrays.toString(undominantlySort(currentPopulation, argsParser.getPopulationCount(), problem).stream().mapToLong(x -> x.stream().count()).toArray()));
                Files.write(Paths.get("izlaz-dec.txt"), currentPopulation.stream().map(x -> Arrays.toString(x.values) + "\n").reduce((a, b) -> a + b).get().getBytes());
                Files.write(Paths.get("izlaz-obj.txt"), currentPopulation.stream().map(x -> Arrays.toString(x.evaluation) + "\n").reduce((a, b) -> a + b).get().getBytes());
                return;
            }

            nextPopulationUsingKTournament(problem, random, currentPopulation, nextPopulation);

            currentPopulation.addAll(nextPopulation);
        }
    }

    private static void calculateFitness(List<Chromosome> currentPopulation, int populationCount, MOOPProblem problem) {
        List<List<Chromosome>> fronts = undominantlySort(currentPopulation, populationCount, problem);

        for (int i = 0; i < fronts.size(); i++) {
            for (Chromosome q : fronts.get(i)) {
                q.rang = i;
            }
        }
        currentPopulation.sort(Comparator.<Chromosome>comparingDouble(x -> x.rang).<Chromosome>thenComparingDouble(x -> -x.crowding));

        if (problem.getDimension() == 2) {
            if (ex != null) {
                ex.newData(fronts);
            }
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static List<List<Chromosome>> undominantlySort(List<Chromosome> currentPopulation, int populationCount, MOOPProblem problem) {
        List<Chromosome> pop = new ArrayList<>();
        List<List<Chromosome>> fronts = new ArrayList<>();
        int k = 0;
        for (Chromosome chromosome : currentPopulation) {
            chromosome.S = new ArrayList<>();
        }
        for (Chromosome chromosomeI : currentPopulation) {
            chromosomeI.eta = 0;
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
                if (fronts.size() == 0) {
                    fronts.add(new ArrayList<>());
                }
                fronts.get(0).add(chromosomeI);
                k = 0;
            }
        }
        List<Chromosome> f1 = fronts.get(0);
        calcCrowding(f1);
        if (f1.size() > populationCount) {
            f1.sort(Comparator.comparing(c -> -c.crowding));
            f1 = f1.subList(0, populationCount - pop.size());
        }
        pop.addAll(f1);

        while (true) {
            List<Chromosome> fi = new ArrayList<>();
            for (int i = 0; i < fronts.get(k).size(); i++) {
                Chromosome frontChromosome = fronts.get(k).get(i);
                for (int j = 0; j < frontChromosome.S.size(); j++) {
                    Chromosome c = frontChromosome.S.get(j);
                    c.eta--;
                    if (c.eta == 0) {
                        fi.add(c);
                    }
                }
            }
            k++;
            if (fi.isEmpty()) {
                break;
            }
            calcCrowding(fi);
            if (fi.size() + pop.size() > populationCount) {
                fi.sort(Comparator.comparing(c -> -c.crowding));
                fi = fi.subList(0, populationCount - pop.size());
            }
            fronts.add(fi);
            pop.addAll(fi);
        }
        currentPopulation.clear();
        currentPopulation.addAll(pop);

        return fronts;
    }

    private static void calcCrowding(List<Chromosome> chromosomes) {
        for (Chromosome c : chromosomes) {
            c.crowding = 0;
        }


        Chromosome chromosome1 = chromosomes.get(0);
        for (int i = 0; i < chromosome1.evaluation.length; i++) {

            final int idx = i;
            Collections.sort(chromosomes, Comparator.comparing(c -> c.evaluation[idx]));
            chromosomes.get(0).crowding = Double.MAX_VALUE;
            chromosomes.get(chromosomes.size() - 1).crowding = Double.MAX_VALUE;

            double delta = chromosomes.get(chromosomes.size() - 1).evaluation[i] - chromosomes.get(0).evaluation[i];
            if (delta != 0) {
                for (int j = 1; j < chromosomes.size() - 1; j++) {
                    chromosomes.get(j).crowding = (chromosomes.get(j + 1).evaluation[i] - chromosomes.get(j - 1).evaluation[i]) / delta;
                }
            }
        }
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

    private static void nextPopulationUsingKTournament(MOOPProblem problem, Random random, List<Chromosome> currentPopulation, List<Chromosome> nextPopulation) {
        nextPopulation.clear();
        for (int i = 0; i < currentPopulation.size(); i++) {
            List<Chromosome> candidates = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                Chromosome randomChromosome;
                do{
                    randomChromosome = getRandomChromosome(random, currentPopulation);
                }while (candidates.contains(randomChromosome));
                candidates.add(randomChromosome);
            }
            candidates.sort(Comparator.<Chromosome>comparingDouble(c->c.rang).<Chromosome>thenComparingDouble(c->-c.crowding));

            Chromosome mama = candidates.get(0);
            Chromosome papa = candidates.get(1);

            tweakChild(problem, random, nextPopulation, mama, papa);
        }
    }

    private static Chromosome getRandomChromosome(Random random, List<Chromosome> currentPopulation) {
        return currentPopulation.get(random.nextInt(currentPopulation.size()));
    }


    private static void tweakChild(MOOPProblem problem, Random random, List<Chromosome> nextPopulation, Chromosome mama, Chromosome papa) {
        Chromosome child = mama.newLikeThis();

        double[] maxs = problem.getSolutionMaxs();
        double[] mins = problem.getSolutionMins();
        for (int j = 0; j < child.values.length; j++) {
            double min = Math.min(mama.values[j], papa.values[j]);
            double max = Math.max(mama.values[j], papa.values[j]);
            child.values[j] = min + (max - min) * random.nextDouble() + random.nextGaussian() * 0.05;
            child.values[j] = Math.max(Math.min(child.values[j], maxs[j]), mins[j]);
        }
        nextPopulation.add(child);
    }

    private static boolean isFinished(int iterCount, ArgsParser argsParser) {
        return iterCount >= argsParser.getMaxIterCount();
    }
}
