package hr.fer.zemris.fuzzy.controller;

import hr.fer.zemris.fuzzy.controller.inference.AkcelFuzzySystemMin;
import hr.fer.zemris.fuzzy.controller.inference.FuzzySystem;
import hr.fer.zemris.fuzzy.controller.inference.RudderFuzzySystemMin;
import org.junit.Test;

/**
 * Created by ivan on 11/9/15.
 */
public class FuzzyControllerTest {

    @Test
    public void test() throws Exception {
        Defuzzifier defuzzifier = new COADefuzzifier();
        FuzzySystem fsAkcel = new AkcelFuzzySystemMin(defuzzifier);
        fsAkcel.verbose = true;
        FuzzySystem fsRudder = new RudderFuzzySystemMin(defuzzifier);
        int L = 50, D = 40, LK = 50, DK = 50, V = 0, S = 0;
        fsRudder.verbose = true;

        System.out.printf("Akcel=%d Rudder=%d\n", fsAkcel.infer(L, D, LK, DK, V, S), fsRudder.infer(L, D, LK, DK, V, S));
    }
}
