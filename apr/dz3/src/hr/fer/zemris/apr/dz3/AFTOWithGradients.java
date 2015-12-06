package hr.fer.zemris.apr.dz3;

/**
 * Created by ivan on 12/6/15.
 */
public abstract class AFTOWithGradients extends AbstractFunctionToOptimize {

    public abstract Point getGradientAtPoint(Point point);
}
