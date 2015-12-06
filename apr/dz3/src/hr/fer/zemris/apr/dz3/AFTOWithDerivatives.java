package hr.fer.zemris.apr.dz3;

import hr.fer.zemris.apr.dz1.Matrix;

/**
 * Created by ivan on 12/6/15.
 */
public abstract class AFTOWithDerivatives extends AbstractFunctionToOptimize {

    private int hessianCallCount = 0;
    private int gradientCallCount = 0;

    public final Matrix hessianAt(Point point) {
        hessianCallCount++;
        return internalHessianAt(point);
    }

    public final Point gradientAt(Point point) {
        gradientCallCount++;
        return internalGradientAt(point);
    }

    protected abstract Point internalGradientAt(Point point);

    protected abstract Matrix internalHessianAt(Point point);

    int dimension(int desired) {
        return desired;
    }

    public int getAfterOptimizationHessianCallCount() {
        int callCount = this.hessianCallCount;
        this.hessianCallCount = 0;
        return callCount;
    }

    public int getAfterOptimizationGradientCallCOunt() {
        int callCount = this.gradientCallCount;
        this.gradientCallCount = 0;
        return callCount;
    }
}
