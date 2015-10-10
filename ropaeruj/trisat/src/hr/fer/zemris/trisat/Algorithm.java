package hr.fer.zemris.trisat;

/**
 * Created by ivan on 10/10/15.
 */
public interface Algorithm {

    SATProblemResults solveSATProblem(SATFormula formula);
}
