package hr.fer.zemris.trisat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by ivan on 10/10/15.
 *
 */
public class SATFormulaFactory {
    /**
     * creates SATFormula object from configuration file
     *
     * @param path path to serialized SAT-formula
     * @return SATFormula
     */
    static SATFormula createFromFile(String path) {

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            int variableNumber = 0;
            int clauseNumber = 0;
            String line = br.readLine();

            while (line != null) {
                if (line.startsWith("p")) {
                    Scanner scanner = new Scanner(line.substring(6));
                    //System.out.println(line.substring(5));
                    variableNumber = scanner.nextInt();
                    clauseNumber = scanner.nextInt();
                    line = br.readLine();
                    scanner.close();
                    break;
                }

                line = br.readLine();
            }
            if (clauseNumber <= 0 || variableNumber <= 0) {
                return null;
            }

            List<Clause> clauses = new ArrayList<>();

            while (line != null) {
                if (line.startsWith("%")) {
                    if (clauseNumber == clauses.size()){
                        Clause[] clausesArray = new Clause[clauses.size()];
                        clausesArray = clauses.toArray(clausesArray);
                        return new SATFormula(variableNumber, clausesArray);
                    } return null;
                } else if (!line.startsWith("c")) {
                    int[] indexes = new int[3];
                    Scanner scanner = new Scanner(line);
                    for (int i = 0; i < 3; i++) {
                        indexes[i] = scanner.nextInt();
                    }
                    clauses.add(new Clause(indexes));
                    scanner.close();
                }

                line = br.readLine();
            }
            return null;
        } catch (IOException e) {
        	System.err.println(e.getMessage());
            return null;
        }
    }
}
