package hr.fer.zemris.optjava.dz2;

import Jama.Matrix;

/**
 * Created by ivan on 10/18/15.
 */
public interface OnStepListener {
    void onStepEntered(Matrix currentOptimalPoint);
}
