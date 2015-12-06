package hr.fer.zemris.apr.dz3;

import hr.fer.zemris.apr.dz1.Matrix;

/**
 * Created by ivan on 12/6/15.
 */
public abstract class AFTOWithHessian  extends AFTOWithGradients{
    public abstract Matrix getHessianAtPoint(Point point);
}
