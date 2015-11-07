package hr.fer.zemris.optjava.dz5.part2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by ivan on 10/23/15.
 */
class FileParser {

    public final double[][] prices;
    public final double[][] distances;
    public final int dimension;

    FileParser(String filePath) {
        File file = new File(filePath);

        double[][] distances = null;
        double[][] prices = null;
        int dimension = 0;

        try (Scanner input = new Scanner(file)) {
            dimension = input.nextInt();
            input.nextLine();
            input.nextLine();

            distances = new double[dimension][dimension];
            prices = new double[dimension][dimension];

            fillMatrix(input, distances);

            input.nextLine();

            fillMatrix(input, prices);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        this.distances = distances;
        this.prices = prices;
        this.dimension = dimension;
    }

    private void fillMatrix(Scanner input, double[][] matrix) {
        int n = matrix.length;
        for (double[] row : matrix) {
            String[] numbers = input.nextLine().trim().replaceAll("[ ]+", " ").split(" ");
            System.arraycopy(Arrays.stream(numbers).mapToDouble(Double::valueOf).toArray(), 0, row, 0, n);
        }
    }
}
