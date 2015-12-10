package hr.fer.zemris.ropaeruj.dz8;


import java.util.Arrays;

/**
 * Created by ivan on 12/10/15.
 */
public abstract class ANN {

    public static ANN create(String type, ITransferFunction hiddenLayerTF, ITransferFunction outputLayerTF) {
        String[] splitted = type.split("-");

        int[] layers = Arrays.stream(splitted[1].split("x")).mapToInt(Integer::valueOf).toArray();
        switch (splitted[0]) {
            case "tdnn":
                return FFANN.create(layers, hiddenLayerTF, outputLayerTF);
            case "elman":
                return ElmanANN.create(layers, hiddenLayerTF);
            default:
                throw new IllegalArgumentException(type);
        }

    }

    public static ANN create(String type) {
        return create(type, new SigmoidTransferFunction(), new SigmoidTransferFunction());
    }

    public abstract void calcOutputs(double[] inputs, double[] weights, double[] outputs);

    public abstract int getInputDimension();
}
