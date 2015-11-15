package hr.fer.zemris.fuzzy.sets;

import java.util.Arrays;

/**
 * Created by ivan on 10/14/15.
 */
public class DomainElement {
    private final int[] values;

    DomainElement(int... values) {
        this.values = Arrays.copyOf(values, values.length);
    }

    public static DomainElement of(int... values) {
        return new DomainElement(values);
    }

    public int getNumberOfComponents() {
        return values.length;
    }

    public int getComponentValue(int idx) {
        return values[idx];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DomainElement that = (DomainElement) o;

        return Arrays.equals(values, that.values);

    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(values);
    }

    @Override
    public String toString() {
        return Arrays.toString(values);
    }

}
