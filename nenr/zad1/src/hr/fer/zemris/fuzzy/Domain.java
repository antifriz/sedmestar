package hr.fer.zemris.fuzzy;

import javax.naming.OperationNotSupportedException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.jar.Pack200;

/**
 * Created by ivan on 10/14/15.
 */
public abstract class Domain implements IDomain {

    public Domain() {
    }

    @Override
    public int indexOfElement(DomainElement element) {
        Iterator<DomainElement> it = iterator();
        int index = 0;
        while (it.hasNext()) {
            DomainElement de = it.next();
            if (de.equals(element)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    @Override
    public DomainElement elementForIndex(int index) {
        Iterator<DomainElement> it = iterator();
        DomainElement de = null;
        for (int i = 0; i <= index; i++) {
            if (it.hasNext()) {
                de = it.next();
            } else {
                throw new IndexOutOfBoundsException();
            }
        }
        if (de == null) {
            throw new IndexOutOfBoundsException();
        }
        return de;
    }

    public static IDomain intRange(int first, int last) {
        return new SimpleDomain(first, last);
    }

    public static Domain combine(IDomain first, IDomain second) {
        int firstNumberOfComponents = first.getNumberOfComponents();
        int secondNumberOfComponents = second.getNumberOfComponents();
        SimpleDomain[] domains = new SimpleDomain[firstNumberOfComponents + secondNumberOfComponents];
        for (int i = 0; i < first.getNumberOfComponents(); i++) {
            IDomain comp = first.getComponent(i);
            if (comp instanceof SimpleDomain) {
                domains[i] = (SimpleDomain) comp;
            } else {
                throw new UnsupportedOperationException("In current implementation this cannot happen");
            }
        }
        for (int i = 0; i < secondNumberOfComponents; i++) {
            IDomain comp = second.getComponent(i);
            if (comp instanceof SimpleDomain) {
                domains[i + first.getNumberOfComponents()] = (SimpleDomain) comp;
            } else {
                throw new UnsupportedOperationException("In current implementation this cannot happen");
            }
        }

        return new CompositeDomain(domains);
    }
}
