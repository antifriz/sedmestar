package hr.fer.zemris.trisat;

/**
 * Created by ivan on 10/10/15.
 * Algorithm interface which must be implemented by every 3-sat solving algorithm
 */
public interface Algorithm {

    /**
     * Method used to solve 3-sat problem.
     * Results are outputted via stdout.
     *
     * @param formula SAT problem to solve
     */
    void solveSATProblem(SATFormula formula);
}
