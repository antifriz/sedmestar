package hr.fer.zemris.ropaeruj.dz7;

/**
 * Created by ivan on 11/16/15.
 */
public class FFANN {

    public FFANN(int[] layers,ITransferFunction[] transferFunctions, IReadOnlyDataset dataset) {
        assert transferFunctions.length +1 == layers.length;
    }

    
}
