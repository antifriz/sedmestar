package hr.fer.zemris.optjava.dz3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by ivan on 10/23/15.
 */
class FileParser {

    double[][] systemMatrix;
    double[] valueVector;

    FileParser(String filePath) {
        File file = new File(filePath);

        systemMatrix = null;
        valueVector = null;

        int lineCount = 0;
        try (Scanner input = new Scanner(file)) {
            while (input.hasNext()) {
                String line = input.nextLine();
                if (line.startsWith("#")) {
                    continue;
                }
                lineCount++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try (Scanner input = new Scanner(file)) {
            double[][] sysMat = null;
            double[] valMat = null;
            int row = 0;
            while (input.hasNext()) {
                String line = input.nextLine().replace(" ", "");
                if (line.startsWith("#")) {
                    continue;
                }
                line = line.substring(1, line.length() - 1);
                String[] idxs = line.split(",");

                int numberOfComponents = idxs.length - 1;
                if (sysMat == null) {
                    sysMat = new double[lineCount][numberOfComponents];
                }
                if (valMat == null) {
                    valMat = new double[lineCount];
                }

                for (int col = 0; col < numberOfComponents; col++) {
                    sysMat[row][col] = Double.parseDouble(idxs[col]);
                }
                sysMat[row][1] *= Math.pow(sysMat[row][0], 3);
                sysMat[row][4] = Math.pow(sysMat[row][4], 2) * sysMat[row][3];


                valMat[row] = Double.parseDouble(idxs[numberOfComponents]);

                row++;
            }
            systemMatrix = sysMat;
            valueVector = valMat;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
