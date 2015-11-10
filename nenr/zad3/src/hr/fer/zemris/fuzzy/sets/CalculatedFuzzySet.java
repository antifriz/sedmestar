package hr.fer.zemris.fuzzy.sets;

/**
 * Created by ivan on 10/15/15.
 */
public class CalculatedFuzzySet implements IFuzzySet {

    private final IDomain mDomain;
    private IIntUnaryFunction mFunction;

    public CalculatedFuzzySet(IDomain domain, IIntUnaryFunction function) {
        mDomain = domain;
        mFunction = function;
    }

    @Override
    public IDomain getDomain() {
        return mDomain;
    }

    @Override
    public double getValueAt(DomainElement element) {
        return mFunction.valueAt(mDomain.indexOfElement(element));
    }
}
