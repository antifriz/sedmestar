package hr.fer.zemris.optjava.dz4.part2;

import java.util.*;

/**
 * Created by ivan on 11/1/15.
 */
public class BoxFragment extends ArrayList<Stick> {

    public static final char FIRST = '\u2591';
    public static final char SECOND = '\u2592';

    public BoxFragment(BoxFragment sticks) {
        super(sticks);
    }

    public BoxFragment() {
        super();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        char character = FIRST;
        for (Stick stick:this ) {
            character = character == FIRST? SECOND : FIRST;
            for (int i = 0; i < stick.height; i++) {
                stringBuilder.append(character);
            }
        }
        return stringBuilder.toString();
    }
}
