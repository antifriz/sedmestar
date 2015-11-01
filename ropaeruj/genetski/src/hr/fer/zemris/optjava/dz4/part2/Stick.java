package hr.fer.zemris.optjava.dz4.part2;

/**
 * Created by ivan on 11/1/15.
 */
public class Stick implements Comparable<Stick> {
    public final int height;
    public final int id;


    public Stick(int height, int id) {
        this.height = height;
        this.id = id;
    }

    @Override
    public String toString() {
        return "["+id+"] "+Integer.toString(height);
    }


    @Override
    public int compareTo(Stick o) {
        return Integer.compare(height, o.height);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Stick && id == ((Stick) o).id;
    }

}
