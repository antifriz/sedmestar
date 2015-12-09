package hr.fer.zemris.ropaeruj.dz8;


/**
 * Created by ivan on 12/10/15.
 */
public abstract class ANN {

    public static ANN create(String type, ITransferFunction transferFunction) {
        String[] splitted = type.split("-");

        int[] layers = new int[splitted.length - 1];
        for (int i = 1; i < splitted.length; i++) {
            layers[i] = Integer.parseInt(splitted[i]);
        }
        switch (splitted[0]) {
            case "tdnn":
                return FFANN.create(layers, transferFunction);
            case "elman":
                return ElmanANN.create(layers, transferFunction);
            default:
                throw new IllegalArgumentException(type);
        }

    }

    public static ANN create(String type) {
        return create(type, new SigmoidTransferFunction());
    }

    public abstract void calcOutputs(double[] inputs, double[] weights, double[] outputs);

    public abstract int getInputDimension();
}
