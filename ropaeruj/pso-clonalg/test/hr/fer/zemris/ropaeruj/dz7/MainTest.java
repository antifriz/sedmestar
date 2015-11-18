package hr.fer.zemris.ropaeruj.dz7;

import org.junit.Test;

/**
 * Created by ivan on 11/16/15.
 */
public class MainTest {
    @Test
    public void testMain() throws Exception {
        Main.main(new String[]{"iris.data","clonalg","20","0.02","1000"});
    }
}
