package ann;


import java.util.Arrays;

/**
 * Created by ivan on 12/10/15.
 */
public abstract class ANN {

    public static ANN create(String type, ITransferFunction transferFunction) {
        String[] splitted = type.split("-");

        int[] layers = Arrays.stream(splitted[1].split("x")).mapToInt(Integer::valueOf).toArray();
        switch (splitted[0]) {
            case "tdnn":
                return FFANN.create(layers, transferFunction);
            case "elman":
                return ElmanANN.create(layers, transferFunction);
            default:
                throw new IllegalArgumentException(type);
        }

    }

    public abstract void calcOutputs(double[] inputs, double[] weights, double[] outputs);

    public abstract int getInputDimension();

    public abstract void reset();

    public abstract int getWeightsCount();
}
