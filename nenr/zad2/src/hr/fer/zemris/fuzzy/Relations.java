package hr.fer.zemris.fuzzy;

/**
 * Created by ivan on 10/25/15.
 */
public class Relations {
    public static boolean isSymmetric(IFuzzySet relation) {
        if (isUTimesURelation(relation)) {
            for (DomainElement de : relation.getDomain()) {
                if (relation.getValueAt(de) != relation.getValueAt(new DomainElement(de.getComponentValue(1), de.getComponentValue(0)))) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public static boolean isReflexive(IFuzzySet relation) {
        if (isUTimesURelation(relation)) {
            for (DomainElement de : relation.getDomain()) {
                DomainElement element = new DomainElement(de.getComponentValue(1), de.getComponentValue(0));
                if (element.getComponentValue(0) == element.getComponentValue(1)) {
                    if (relation.getValueAt(element) != 1) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public static boolean isMaxMinTransitive(IFuzzySet relation) {
        if (isUTimesURelation(relation)) {
            IBinaryFunction or = Operations.zadehOr();
            IBinaryFunction and = Operations.zadehAnd();
            IDomain domain = relation.getDomain().getComponent(0);
            for (DomainElement x : domain) {
                for (DomainElement z : domain) {
                    DomainElement xz = new DomainElement(x.getComponentValue(0), z.getComponentValue(0));
                    double normValue = 0;
                    for (DomainElement y : domain) {
                        normValue = or.valueAt(normValue, and.valueAt(relation.getValueAt(new DomainElement(x.getComponentValue(0), y.getComponentValue(0))), relation.getValueAt(new DomainElement(y.getComponentValue(0), z.getComponentValue(0)))));
                    }
                    if (relation.getValueAt(xz) < normValue) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public static IFuzzySet compositionOfBinaryRelations(IFuzzySet relation1, IFuzzySet relation2) {
        SimpleDomain U = (SimpleDomain) relation1.getDomain().getComponent(0);
        SimpleDomain V = (SimpleDomain) relation1.getDomain().getComponent(1);
        SimpleDomain W = (SimpleDomain) relation2.getDomain().getComponent(1);

        MutableFuzzySet set = new MutableFuzzySet(new CompositeDomain(U,W));

        IBinaryFunction or = Operations.zadehOr();
        IBinaryFunction and = Operations.zadehAnd();

        for (DomainElement u : U) {
            for (DomainElement w : W) {
                double value = 0;
                for (DomainElement v : V) {
                    value = or.valueAt(value,and.valueAt(relation1.getValueAt(new DomainElement(u.getComponentValue(0),v.getComponentValue(0))),relation2.getValueAt(new DomainElement(v.getComponentValue(0),w.getComponentValue(0)))));
                }

                set.set(DomainElement.of(u.getComponentValue(0),w.getComponentValue(0)),value);
            }
        }

        return set;
    }

    public static boolean isFuzzyEquivalence(IFuzzySet relation) {
        return isReflexive(relation) && isSymmetric(relation) && isMaxMinTransitive(relation);
    }

    public static boolean isUTimesURelation(IFuzzySet relation) {
        if (relation.getDomain().getNumberOfComponents() == 2) {
            if (relation.getDomain().getComponent(0).equals(relation.getDomain().getComponent(1))) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
