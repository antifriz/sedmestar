package hr.fer.zemris.fuzzy;

/**
 * Created by ivan on 10/14/15.
 */
public interface IFuzzySet {
    IDomain getDomain();
    double getValueAt(DomainElement element);
}
