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


    public static final int MAX_ACCELERATION = 300;
    public static final int MAX_DISTANCE = 1200;
    public static final int MAX_SPEED = 1000;
    private static final int MAX_ANGLE = 60;

    enum RelativeDistance {
        NEGATIVE,
        ZERO,
        POSITIVE
    }

    enum Velocity {
        SLOW,
        FAST
    }

    enum Acceleration {
        NEGATIVE,
        ZERO,
        POSITIVE
    }

    @Test
    public void testPlayground() throws Exception {
        int D = 50, L = 50, LK = 50, DK = 50, kormilo = 0, akcel = 0, V = 0;
        Random random = new Random();
        for (int i = 0; i < 1000; i++) {
            D=DK= random.nextInt(100);
            L = LK=random.nextInt(100);
            V = random.nextInt(100);
            int ld = L - D;
            int lkdk = LK - DK;
            Pair<Integer, Integer> infer = FuzzySystem.infer(ld, lkdk, V);
            if(lkdk>0 && infer.getValue()<0){
                System.out.printf("L=%d D=%d V=%d\n",L,D,V);
                Assert.assertTrue(false);
            }
            if(lkdk<0 && infer.getValue()>0){
                System.out.printf("L=%d D=%d V=%d\n",L,D,V);
                Assert.assertTrue(false);
            }



        }
//        System.out.println(infer.getKey()+" "+infer.getValue());
    }

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
