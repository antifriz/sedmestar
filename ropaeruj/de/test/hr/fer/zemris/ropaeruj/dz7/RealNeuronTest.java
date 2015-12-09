package hr.fer.zemris.ropaeruj.dz7;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by ivan on 11/17/15.
 */
public class RealNeuronTest {

    @Test
    public void testGetOutput() throws Exception {
        FFANN.RealNeuron neuron = new FFANN.RealNeuron(0,0,4,new SigmoidTransferFunction());
        assertEquals(neuron.getOutput(new double[]{1,1,1,1},new double[]{1,1,-1,-1}),0.5,1e-1);
    }
}