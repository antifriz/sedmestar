package hr.fer.zemris.optjava.dz4.part2;

/**
 * Created by ivan on 11/1/15.
 */
public class Stick implements Comparable<Stick> {
    public final int height;


    public Stick(int height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return Integer.toString(height);
    }


    @Override
    public int compareTo(Stick o) {
        return Integer.compare(height, o.height);
    }
}
