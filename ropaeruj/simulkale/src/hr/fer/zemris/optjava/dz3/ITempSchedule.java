package hr.fer.zemris.optjava.dz3;

/**
 * Created by ivan on 10/22/15.
 */
public interface ITempSchedule {
    void reset();

    double getNextTemperature();

    int getInnerLoopCounter();

    int getOuterLoopCounter();
}
