package hr.fer.zemris.fuzzy.sets;

/**
 * Created by ivan on 10/14/15.
 */
public interface IFuzzySet {
    IDomain getDomain();

    double getValueAt(DomainElement element);

    default double getValueAt(int... indexes){
        return getValueAt(new DomainElement(indexes));
    }
}
