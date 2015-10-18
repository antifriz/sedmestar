package hr.fer.zemris.optjava.dz2;

import Jama.Matrix;
import hr.fer.zemris.optjava.dz2.NumOptAlgorithms.Algorithm;

/**
 * Created by ivan on 10/18/15.
 */
public class Jednostavno {
    public static void main(String[] args) {
        ArgsParser argsParser = new ArgsParser(args);


        TrajectoryImage trajectoryImage = new TrajectoryImage(500, 500, -5.0, +5.0, -5.0, +5.0, argsParser.getInitialX(), argsParser.getInitialY());
        TrajectoryTask trajectoryTask = new TrajectoryTask() {
            @Override
            public Matrix solve(Matrix initialPoint, int maxIterCount) {
                return NumOptAlgorithms.runAlgorithm(initialPoint, argsParser.getFunction(), maxIterCount, trajectoryImage, argsParser.getAlgorithm());
            }
        };
        trajectoryImage.setFunction(argsParser.getFunction());

        trajectoryTask.run(argsParser.getInitialX(), argsParser.getInitialY(), argsParser.getMaxIterCount());

        trajectoryImage.tryWrite("PNG", args[0] + ".png");
    }

    private static class ArgsParser {

        private final int mMaxIterCount;
        private final IHFunction mFunction;
        private final Algorithm mAlgorithm;
        private double mInitialX;
        private double mInitialY;

        public ArgsParser(String[] args) {
            if ((args.length != 2 && args.length != 4) || args[0].length() != 2) {
                System.err.printf("Parameters: task_name max_iter_count [starting_point_x starting_point_y]");
                System.exit(0);
            }
            RandomUtils utils = RandomUtils.get();
            mInitialX = utils.nextDoubleInRange(-5, 5);
            mInitialY = utils.nextDoubleInRange(-5, 5);

            if (args.length == 4) {
                mInitialX = Float.parseFloat(args[2]);
                mInitialY = Float.parseFloat(args[3]);
            }

            mMaxIterCount = Integer.parseInt(args[1]);

            switch (args[0].charAt(0)) {
                case '2':
                    mFunction = new ElipsoidalFunction();
                    break;
                case '1':
                default:
                    mFunction = new SphericalFunction();
                    break;
            }
            switch (args[0].charAt(1)) {
                case 'b':
                    mAlgorithm = Algorithm.NEWTON_METHOD;
                    break;
                case 'a':
                default:
                    mAlgorithm = Algorithm.GRADIENT_DESCENT;
                    break;
            }
        }

        public int getMaxIterCount() {
            return mMaxIterCount;
        }


        public double getInitialX() {
            return mInitialX;
        }

        public double getInitialY() {
            return mInitialY;
        }

        public IHFunction getFunction() {
            return mFunction;
        }

        public Algorithm getAlgorithm() {
            return mAlgorithm;
        }
    }
}
