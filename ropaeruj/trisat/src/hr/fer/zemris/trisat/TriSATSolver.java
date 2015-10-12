package hr.fer.zemris.trisat;

public class TriSATSolver {

    public static void main(String[] args) {
        ArgumentsParser parser = ArgumentsParser.createArgumentsParser();
        if(!parser.parseArguments(args)){
            System.out.println("Usage:\nsolver [algorithm-id] [config-file]");
            System.exit(1);
        }

        Algorithm algorithm = parser.getAlgorithm();
        SATFormula formula = parser.getSATFormula();

        algorithm.solveSATProblem(formula);
    }

}
