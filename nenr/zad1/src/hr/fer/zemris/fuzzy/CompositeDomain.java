package hr.fer.zemris.fuzzy;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by ivan on 10/14/15.
 */
public class CompositeDomain extends Domain {
    private SimpleDomain[] mDomains;

    public CompositeDomain(SimpleDomain... domains) {
        mDomains = Arrays.copyOf(domains, domains.length);
    }

    @Override
    public int getCardinality() {
        return Arrays.stream(mDomains).mapToInt(SimpleDomain::getCardinality).reduce(1, (x, y) -> x * y);
    }

    @Override
    public IDomain getComponent(int idx) {
        return mDomains[idx];
    }

    @Override
    public int getNumberOfComponents() {
        return mDomains.length;
    }

    @Override
    public Iterator<DomainElement> iterator() {
        return new Iterator<DomainElement>() {
            private final Iterator<DomainElement>[] mIterators = Arrays.stream(mDomains).map(SimpleDomain::iterator).toArray(Iterator[]::new);
            int mSize = mDomains.length;
            private final int[] mValues = Arrays.copyOf(Arrays.stream(mIterators).limit(mSize - 1).mapToInt(x -> x.hasNext() ? x.next().getComponentValue(0) : 0).toArray(), mSize);


            @Override
            public boolean hasNext() {
                return Arrays.stream(mIterators).anyMatch(Iterator::hasNext);
            }

            @Override
            public DomainElement next() {
                int current;
                for (current = mSize - 1; current >= 0; current--) {
                    if (mIterators[current].hasNext()) {
                        break;
                    }
                }
                for (int i = current + 1; i < mSize; i++) {
                    mIterators[i] = mDomains[i].iterator();
                }

                for (int i = current; i < mSize; i++) {
                    setNextValue(i);
                }

                return new DomainElement(mValues);
            }

            private void setNextValue(int index) {
                Iterator<DomainElement> it = mIterators[index];
                if (it.hasNext()) {
                    mValues[index] = it.next().getComponentValue(0);
                }
            }
        };
    }
}
