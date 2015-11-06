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

    FileParser(String filePath) {
        File file = new File(filePath);

        double[][] distances = null;
        double[][] prices = null;

        try (Scanner input = new Scanner(file)) {
            int n = input.nextInt();
            input.nextLine();
            input.nextLine();

            distances = new double[n][n];
            prices = new double[n][n];

            fillMatrix(input, distances);

            input.nextLine();

            fillMatrix(input, prices);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        this.distances = distances;
        this.prices = prices;
    }

    private void fillMatrix(Scanner input, double[][] matrix) {
        int n = matrix.length;
        for (double[] row : matrix) {
            String[] numbers = input.nextLine().replace("  ", " ").split(" ");
            System.arraycopy(Arrays.stream(numbers).mapToDouble(Double::valueOf).toArray(), 0, row, 0, n);
        }
    }
}
