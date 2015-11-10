package hr.fer.zemris.fuzzy.sets;

/**
 * Created by ivan on 10/15/15.
 */
public class MutableFuzzySet implements IFuzzySet {
    private double[] mMemberships;
    private IDomain mDomain;

    public MutableFuzzySet(IDomain domain) {
        mDomain = domain;
        mMemberships = new double[domain.getCardinality()];
    }

    @Override
    public IDomain getDomain() {
        return mDomain;
    }

    @Override
    public double getValueAt(DomainElement element) {
        return mMemberships[mDomain.indexOfElement(element)];
    }

    public MutableFuzzySet set(DomainElement element, double value) {
        mMemberships[mDomain.indexOfElement(element)] = value;
        return this;
    }
}
