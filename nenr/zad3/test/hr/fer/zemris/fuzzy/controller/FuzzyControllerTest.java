package hr.fer.zemris.fuzzy.controller;

import hr.fer.zemris.fuzzy.sets.*;
import javafx.util.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by ivan on 11/9/15.
 */
public class FuzzyControllerTest {

    @Test
    public void testName() throws Exception {
        int D = 50, L = 50, LK = 50, DK = 50, kormilo = 0, akcel = 0, V = 10;

        if (D - L > 20) {
            kormilo = 20;
            akcel = +20;
        } else if (D - L < -20) {
            kormilo = -20;
            akcel = +20;
        } else {
            kormilo = 0;
            akcel = -20;
        }

        if (DK - LK > 20) {
            kormilo += 20;
            akcel += 20;
        } else if (DK - LK < -20) {
            kormilo -= 20;
            akcel += 20;
        } else {
            akcel -= 20;
        }

        if (V == 0) {
            akcel += 40;
        }

        akcel += 1;
        System.out.println(akcel);
        System.out.println(kormilo);
    }
}
