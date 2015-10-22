package hr.fer.zemris.fuzzy;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by ivan on 10/22/15.
 */
public class Tests {
    private final ByteArrayOutputStream mByteArrayOutputStream = new ByteArrayOutputStream();
    private final PrintStream mOut = System.out;

    @Before
    public void setUp() throws Exception {
        System.setOut(new PrintStream(mByteArrayOutputStream));
    }

    private void assertEqualsOut(String path) {
        try (Scanner s = new Scanner(new File(path)).useDelimiter("\\Z")) {
            String string = mByteArrayOutputStream.toString().trim();
            assertEquals(s.next().trim(), string);
            mOut.print(string);
        } catch (FileNotFoundException e) {

        }
    }

    @Test
    public void domene() {
        IDomain d1 = Domain.intRange(0, 5); // {0,1,2,3,4}
        Debug.print(d1, "Elementi domene d1:");
        IDomain d2 = Domain.intRange(0, 3); // {0,1,2}
        Debug.print(d2, "Elementi domene d2:");
        IDomain d3 = Domain.combine(d1, d2);
        Debug.print(d3, "Elementi domene d3:");
        System.out.println(d3.elementForIndex(0));
        System.out.println(d3.elementForIndex(5));
        System.out.println(d3.elementForIndex(14));
        System.out.println(d3.indexOfElement(DomainElement.of(4, 1)));

        assertEqualsOut("domene.txt");
    }

    @Test
    public void primjer1() {
        IDomain d = Domain.intRange(0, 11); // {0,1,...,10}
        IFuzzySet set1 = new MutableFuzzySet(d)
                .set(DomainElement.of(0), 1.0)
                .set(DomainElement.of(1), 0.8)
                .set(DomainElement.of(2), 0.6)
                .set(DomainElement.of(3), 0.4)
                .set(DomainElement.of(4), 0.2);
        Debug.print(set1, "Set1:");
        IDomain d2 = Domain.intRange(-5, 6); // {-5,-4,...,4,5}
        IFuzzySet set2 = new CalculatedFuzzySet(
                d2,
                StandardFuzzySets.lambdaFunction(
                        d2.indexOfElement(DomainElement.of(-4)),
                        d2.indexOfElement(DomainElement.of(0)),
                        d2.indexOfElement(DomainElement.of(4))
                )
        );
        Debug.print(set2, "Set2:");

        assertEqualsOut("primjer1.txt");
    }

    @Test
    public void primjer2() {
        IDomain d = Domain.intRange(0, 11);
        IFuzzySet set1 = new MutableFuzzySet(d)
                .set(DomainElement.of(0), 1.0)
                .set(DomainElement.of(1), 0.8)
                .set(DomainElement.of(2), 0.6)
                .set(DomainElement.of(3), 0.4)
                .set(DomainElement.of(4), 0.2);
        Debug.print(set1, "Set1:");
        IFuzzySet notSet1 = Operations.unaryOperation(
                set1, Operations.zadehNot());
        Debug.print(notSet1, "notSet1:");
        IFuzzySet union = Operations.binaryOperation(
                set1, notSet1, Operations.zadehOr());
        Debug.print(union, "Set1 union notSet1:");
        IFuzzySet hinters = Operations.binaryOperation(
                set1, notSet1, Operations.hamacherTNorm(1.0));
        Debug.print(hinters, "Set1 intersection with notSet1 using parameterised Hamacher T norm with parameter 1.0:");

        assertEqualsOut("primjer2.txt");
    }

    @After
    public void tearDown() throws Exception {
        System.setOut(mOut);
    }
}
