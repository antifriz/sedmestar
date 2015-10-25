package hr.fer.zemris.fuzzy;

/**
 * Created by ivan on 10/14/15.
 */
public class Debug {
    public static void print(IDomain domain, String headingText) {
        if (headingText != null) {
            System.out.println(headingText);
        }
        for (DomainElement e : domain) {
            System.out.printf("Element domene: %s\n", e);
        }
        System.out.println("Kardinalitet domene je: " + domain.getCardinality());
        System.out.println();
    }

    public static void print(IFuzzySet set, String headingText) {
        if (headingText != null) {
            System.out.println(headingText);
        }
        IDomain domain = set.getDomain();
        for (DomainElement e : domain) {
            String s = e.toString();
            System.out.printf("d(%s)=%8.6f\n", s.substring(1, s.length() - 1), set.getValueAt(e));
        }
        System.out.println();
    }
}