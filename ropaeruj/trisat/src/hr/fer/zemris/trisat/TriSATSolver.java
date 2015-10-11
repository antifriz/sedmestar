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

        //System.out.println("Using algorithm "+algorithm.getClass().getSimpleName());
        //System.out.println("Using formula:\n"+ args2);

        algorithm.solveSATProblem(formula);
    }

}
