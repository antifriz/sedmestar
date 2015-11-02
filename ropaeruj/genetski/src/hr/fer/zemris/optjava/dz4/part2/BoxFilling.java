package hr.fer.zemris.optjava.dz4.part2;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by ivan on 10/31/15.
 * <p>
 * preporucam set parametara: 02-zad-prijenosna.txt 100 0 2000 tournament:7 1
 */
public class BoxFilling {

    public static void main(String[] args) {

        ArgsParser argsParser = new ArgsParser(args);

        FileParser fileParser = new FileParser(argsParser.getFileName());

        Random random = new Random();

        BoxChromosome[] population = new BoxChromosome[argsParser.getPopulationCount()];

        for (int i = 0; i < population.length; i++) {
            population[i] = new BoxChromosome(fileParser.height);
            Collections.shuffle(fileParser.sticks);
            population[i].fill(fileParser.sticks);
        }

        evaluate(population);

        for (int i = 0; i < argsParser.getMaxIterCount(); i++) {
            Arrays.sort(population, Collections.<BoxChromosome>reverseOrder());


            print(population[0], "best");

            if (isFinished(population, argsParser)) {
                System.out.println("finished");
                break;
            }

            BoxChromosome mama = selectBetter(population, random, argsParser.getN());
            BoxChromosome papa = selectBetter(population, random, argsParser.getN());

            BoxChromosome mamaCpy = mama.duplicate();
            BoxChromosome papaCpy = papa.duplicate();

            int pointA = random.nextInt(mama.size());
            int pointB = random.nextInt(mama.size());
            int mamaCrossoverStartPoint = Math.min(pointA, pointB);
            int mamaCrossoverEndPoint = Math.max(pointA, pointB);

            List<BoxFragment> poppedFromMama = mamaCpy.rip(mamaCrossoverStartPoint, mamaCrossoverEndPoint);

            int pointC = random.nextInt(papa.size());
            int pointD = random.nextInt(papa.size());
            int papaCrossoverStartPoint = Math.min(pointC, pointD);
            int papaCrossoverEndPoint = Math.max(pointC, pointD);

            List<BoxFragment> poppedFromPapa = papaCpy.rip(papaCrossoverStartPoint, papaCrossoverEndPoint);

            mamaCpy.insert(poppedFromPapa, mamaCrossoverStartPoint);
            papaCpy.insert(poppedFromMama, papaCrossoverStartPoint);

            mamaCpy.mutate(random);
            papaCpy.mutate(random);

            mamaCpy.evaluateSelf();
            papaCpy.evaluateSelf();

            BoxChromosome child = mamaCpy.mFitness > papaCpy.mFitness ? mamaCpy : papaCpy;

            insertChild(child, population, random, argsParser.getM());
        }

    }

    private static void print(BoxChromosome chromosome, String heading) {
        System.out.printf("%s Fitness: %f Size: %d\n",heading,chromosome.mFitness,chromosome.size());
        System.out.println(chromosome);
        System.out.println();
    }

    private static void insertChild(BoxChromosome child, BoxChromosome[] population, Random random, Integer m) {
        BoxChromosome[] candidates = new BoxChromosome[m];
        for (int j = 0; j < m; j++) {
            candidates[j] = population[random.nextInt(population.length)];
        }

        Arrays.sort(candidates);

        for (int i = 0; i < population.length; i++) {
            if (population[i] == candidates[0]) {
                population[i] = child;
                return;
            }
        }
        throw new RuntimeException();
    }

    private static BoxChromosome selectBetter(BoxChromosome[] population, Random random, Integer n) {
        BoxChromosome[] candidates = new BoxChromosome[n];
        for (int j = 0; j < n; j++) {
            candidates[j] = population[random.nextInt(population.length)];
        }

        Arrays.sort(candidates, Collections.<BoxChromosome>reverseOrder());

        return candidates[0];
    }

    private static boolean isFinished(BoxChromosome[] population, ArgsParser argsParser) {
        return population[0].size() <= argsParser.getSatisfiableContainerSize() || population[0].mFitness == 1;
    }

    private static void evaluate(BoxChromosome[] population) {
        Arrays.stream(population).parallel().forEach(BoxChromosome::evaluateSelf);
    }
}
