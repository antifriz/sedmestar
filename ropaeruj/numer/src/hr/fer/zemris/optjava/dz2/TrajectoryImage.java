package hr.fer.zemris.optjava.dz2;

import Jama.Matrix;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by ivan on 10/18/15.
 */
public class TrajectoryImage implements OnStepListener {
    private final BufferedImage mBufferedImage;
    private final GeneralPath mGeneralPath;
    private final int mWidth;
    private final int mHeight;
    private final double mFromX;
    private final double mToX;
    private final double mFromY;
    private final double mToY;
    private IFunction mFunction;

    public TrajectoryImage(int width, int height, double fromX, double toX, double fromY, double toY, double initialX, double initialY) {
        mWidth = width;
        mHeight = height;
        mFromX = fromX;
        mToX = toX;
        mFromY = fromY;
        mToY = toY;
        mBufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        mGeneralPath = new GeneralPath(GeneralPath.WIND_NON_ZERO);
        mGeneralPath.moveTo(initialX, initialY);
    }

    public void setFunction(IFunction function) {
        mFunction = function;
    }

    public void tryWrite(String extension, String path) {
        updateImage();
        try {
            ImageIO.write(mBufferedImage, extension, new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateImage() {
        Graphics2D graphics2D = mBufferedImage.createGraphics();
        double sx = mWidth / (mToX - mFromX);
        double sy = mHeight / (mToY - mFromY);

//        AffineTransform oldTransform = graphics2D.getTransform();
        graphics2D.scale(sx, sy);
        graphics2D.translate(-mFromX, -mFromY);
        graphics2D.scale(1, -1);
        graphics2D.setStroke(new BasicStroke(1 / (float) sx));
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        for (double i = mFromX; i < mToX; i += (mToX - mFromX) / mWidth) {
            for (double j = mFromY; j < mToY; j += (mToY - mFromY) / mHeight) {
                double val = mFunction.valueAt(new Matrix(new double[][]{{i}, {j}}));
                min = Math.min(min, val);
                max = Math.max(max, val);
            }
        }
        for (double i = mFromX; i < mToX; i += (mToX - mFromX) / mWidth) {
            for (double j = mFromY; j < mToY; j += (mToY - mFromY) / mHeight) {
                double val = mFunction.valueAt(new Matrix(new double[][]{{i}, {j}}));
                double a = (val - min) / (max - min) * 255;
                int valI = (int) Math.round(a);
                graphics2D.setColor(new Color(valI, valI, valI));
                GeneralPath path = new GeneralPath();
                path.moveTo(i, j);
                path.lineTo(i, j);
                graphics2D.draw(path);
            }
        }
        graphics2D.setColor(Color.red);
        graphics2D.draw(mGeneralPath);
        graphics2D.setColor(new Color(255,128,128));
        graphics2D.drawLine(0, (int) mFromY,0, (int) mToY);
        graphics2D.drawLine ((int) mFromX,0, (int) mToX,0);

        graphics2D.dispose();
    }

    @Override
    public void onStepEntered(Matrix currentOptimalPoint) {
        double currentX = currentOptimalPoint.get(0, 0);
        double currentY = currentOptimalPoint.get(1, 0);
        mGeneralPath.lineTo(
                currentX,
                currentY
        );
    }
}
