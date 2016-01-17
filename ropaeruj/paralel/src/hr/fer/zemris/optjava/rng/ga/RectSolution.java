package hr.fer.zemris.optjava.rng.ga;

import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.RNG;

import java.util.Arrays;

/**
 * Created by ivan on 1/16/16.
 */
public class RectSolution extends GASolution<int[]> {

    public static final int COLOR_LIMIT = 255;
    public final int[] limits;
    private final int rectCount;
    private final int width;
    private final int height;

    public RectSolution(int rectCount, int width, int height) {
        this.rectCount = rectCount;
        this.width = width;
        this.height = height;
        int sumaSumarum = rectCount * 5 + 1;
        data = new int[sumaSumarum];
        limits = new int[sumaSumarum];
        limits[0] = COLOR_LIMIT;
        for (int i = 1; i <= rectCount*5; i+=5) {
            limits[i] = limits[i+1] = width;
            limits[i+2] = limits[i+3] = height;
            limits[i+4] = COLOR_LIMIT;
        }
        IRNG rng = RNG.getRNG();
        for (int i = 0; i < data.length; i++) {
            data[i] = rng.nextInt(0, limits[i]);
        }
    }

    @Override
    public GASolution<int[]> duplicate() {
        int[] data = getData();
        GASolution<int[]> newOne = new RectSolution(rectCount, width, height);
        newOne.data = Arrays.copyOf(data, data.length);
        return newOne;
    }

    @Override
    public String toString() {
        return /*Arrays.toString(data) + */" " + fitness;
    }
}
