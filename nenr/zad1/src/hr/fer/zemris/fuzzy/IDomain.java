package hr.fer.zemris.fuzzy;

/**
 * Created by ivan on 10/14/15.
 */
public interface IDomain extends Iterable<DomainElement> {
    int getCardinality();
    IDomain getComponent(int idx);
    int getNumberOfComponents();
    int indexOfElement(DomainElement element);
    DomainElement elementForIndex(int index);
}