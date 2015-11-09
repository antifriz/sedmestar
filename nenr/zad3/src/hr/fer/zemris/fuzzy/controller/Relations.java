package hr.fer.zemris.fuzzy.controller;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created by ivan on 10/25/15.
 */
public class Relations {
    public static boolean isSymmetric(IFuzzySet relation) {
        return isUTimesURelation(relation) && stream(relation.getDomain()).allMatch(de -> relation.getValueAt(de) == relation.getValueAt(new DomainElement(de.getComponentValue(1), de.getComponentValue(0))));
    }

    public static boolean isReflexive(IFuzzySet relation) {
        return isUTimesURelation(relation) && stream(relation.getDomain().getComponent(0)).allMatch(x -> relation.getValueAt(new DomainElement(x.getComponentValue(0), x.getComponentValue(0))) == 1);
    }

    public static IFuzzySet compositionOfBinaryRelations(IFuzzySet relation1, IFuzzySet relation2) {
        SimpleDomain U = (SimpleDomain) relation1.getDomain().getComponent(0);
        SimpleDomain V = (SimpleDomain) relation1.getDomain().getComponent(1);
        SimpleDomain W = (SimpleDomain) relation2.getDomain().getComponent(1);

        MutableFuzzySet set = new MutableFuzzySet(new CompositeDomain(U, W));

        stream(U).forEach(u -> stream(W).forEach(w -> set.set(DomainElement.of(u.getComponentValue(0), w.getComponentValue(0)),
                stream(V)
                        .mapToDouble(v -> Operations.zadehAnd().valueAt(relation1.getValueAt(new DomainElement(u.getComponentValue(0), v.getComponentValue(0))), relation2.getValueAt(new DomainElement(v.getComponentValue(0), w.getComponentValue(0)))))
                        .reduce(Operations.zadehOr()::valueAt).getAsDouble())));

        return set;
    }

    public static boolean isFuzzyEquivalence(IFuzzySet relation) {
        return isReflexive(relation) && isSymmetric(relation) && isMaxMinTransitive(relation);
    }

    public static boolean isUTimesURelation(IFuzzySet relation) {
        return relation.getDomain().getNumberOfComponents() == 2 && relation.getDomain().getComponent(0).equals(relation.getDomain().getComponent(1));
    }

    private static <T> Stream<T> stream(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), true);
    }

    public static boolean isMaxMinTransitive(IFuzzySet relation) {
        return isUTimesURelation(relation) && stream(relation.getDomain().getComponent(0)).allMatch(x -> stream(relation.getDomain().getComponent(0)).allMatch(z -> relation.getValueAt(new DomainElement(x.getComponentValue(0), z.getComponentValue(0))) >= stream(relation.getDomain().getComponent(0)).mapToDouble(item -> Operations.zadehAnd().valueAt(relation.getValueAt(new DomainElement(x.getComponentValue(0), item.getComponentValue(0))), relation.getValueAt(new DomainElement(item.getComponentValue(0), z.getComponentValue(0))))).reduce(Operations.zadehOr()::valueAt).getAsDouble()));
    }
}
