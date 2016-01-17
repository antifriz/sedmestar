package hr.fer.zemris.optjava.rng.ga;

import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.IRNGProvider;
import hr.fer.zemris.optjava.rng.RNG;
import hr.fer.zemris.optjava.rng.ga.Evaluator;

/**
 * Created by ivan on 1/16/16.
 */
public class EvoThread extends Thread implements IRNGProvider {
    private IRNG rng = RNG.getIRNGInstance();


    public EvoThread() {
    }
    public EvoThread(Runnable target) {
        super(target);
    }
//
//    public EvoThread(String name) {
//        super(name);
//    }
//
//    public EvoThread(ThreadGroup group, Runnable target) {
//        super(group, target);
//    }
//
//    public EvoThread(ThreadGroup group, String name) {
//        super(group, name);
//    }
//
//    public EvoThread(Runnable target, String name) {
//        super(target, name);
//    }
//
//    public EvoThread(ThreadGroup group, Runnable target, String name) {
//        super(group, target, name);
//    }
//
//    public EvoThread(ThreadGroup group, Runnable target, String name,
//                     long stackSize) {
//        super(group, target, name, stackSize);
//    }

    @Override
    public IRNG getRNG() {
        return rng;
    }

}
