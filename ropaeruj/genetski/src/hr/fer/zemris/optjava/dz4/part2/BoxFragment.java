package hr.fer.zemris.optjava.dz4.part2;

import java.util.*;

/**
 * Created by ivan on 11/1/15.
 */
public class BoxFragment extends ArrayList<Stick> {

    public BoxFragment(BoxFragment sticks) {
        addAll(sticks);
    }

    public BoxFragment() {

    }
}
