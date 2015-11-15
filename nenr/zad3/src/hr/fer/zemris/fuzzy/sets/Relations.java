package hr.fer.zemris.fuzzy.sets;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created by ivan on 10/25/15.
 */
class Relations {

    private static boolean isUTimesURelation(IFuzzySet relation) {
        return relation.getDomain().getNumberOfComponents() == 2 && relation.getDomain().getComponent(0).equals(relation.getDomain().getComponent(1));
    }

    private static <T> Stream<T> stream(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), true);
    }

}
