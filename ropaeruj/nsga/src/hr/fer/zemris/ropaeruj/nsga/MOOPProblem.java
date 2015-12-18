package hr.fer.zemris.ropaeruj.nsga;

/**
 * Created by ivan on 12/18/15.
 */
public interface MOOPProblem {
    int getNumberOfObjectives();
    void evaluateSolution(double[] solution, double[] objectives);

    double[] getSolutionMaxs();

    double[] getSolutionMins();

    default int getDimension(){
        return getSolutionMaxs().length;
    }
}
