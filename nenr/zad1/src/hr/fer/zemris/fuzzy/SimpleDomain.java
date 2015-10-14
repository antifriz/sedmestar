package hr.fer.zemris.fuzzy;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by ivan on 10/14/15.
 */
public class SimpleDomain extends Domain {
    private int first;
    private int last;

    public SimpleDomain(int first, int last) {
        this.first = first;
        this.last = last;
    }

    @Override
    public int getCardinality() {
        return last - first;
    }

    @Override
    public IDomain getComponent(int idx) {
        return this;
    }

    @Override
    public int getNumberOfComponents() {
        return 1;
    }

    @Override
    public Iterator<DomainElement> iterator() {
        return new Iterator<DomainElement>() {
            private int current = first;

            @Override
            public boolean hasNext() {
                return current < last;
            }

            @Override
            public DomainElement next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return new DomainElement(current++);
            }
        };
    }

    public int getFirst() {
        return first;
    }

    public int getLast() {
        return last;
    }
}
