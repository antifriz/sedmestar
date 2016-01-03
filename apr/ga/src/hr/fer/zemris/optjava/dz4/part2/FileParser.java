package hr.fer.zemris.optjava.dz4.part2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by ivan on 10/23/15.
 */
final class FileParser {

    List<Stick> sticks;
    int height;

    FileParser(String filePath) {
        File file = new File(filePath);

        height = Integer.parseInt(filePath.split("-")[1]);

        sticks = new ArrayList<>();
        try (Scanner input = new Scanner(file)) {
            String line = input.nextLine().replace(" ", "");
            line = line.substring(1, line.length() - 1);
            String[] vals = line.split(",");
            for (int i = 0; i < vals.length; i++) {
                String val = vals[i];
                sticks.add(new Stick(Integer.valueOf(val), i));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
