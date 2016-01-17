package hr.fer.zemris.apr.dz5;

import hr.fer.zemris.apr.dz1.Matrix;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ivan on 1/17/16.
 */
public abstract class DiffMethod {


    protected final Matrix A;
    protected final Matrix B;
    protected final Matrix X0;
    protected final double T;
    protected final double tmax;
    protected final int iterStep;

    protected DiffMethod(Matrix A, Matrix B, Matrix X0, double T, double tmax, int iterStep) {

        this.A = A;
        this.B = B;
        this.X0 = X0;
        this.T = T;
        this.tmax = tmax;
        this.iterStep = iterStep;
    }

    public static DiffMethod createRungeKutta(Matrix A, Matrix B, Matrix X0, double T, double tmax, int iterStep){
        return new RungeKutta(A, B, X0, T, tmax, iterStep);
    }
    public static DiffMethod createTrapezoidalMethod(Matrix A, Matrix B, Matrix X0, double T, double tmax, int iterStep){
        return new TrapezoidalMethod(A, B, X0, T, tmax, iterStep);
    }


    public void run() {
        ArrayList<Matrix> rezultati = new ArrayList<>();
        rezultati.add(X0);
        System.out.println("t=0.0 X0:");
        System.out.println(X0);

        double t = 0.0;
        int iter = 1;
        while (t <= tmax) {
            Matrix xk1 = calculateXk1(rezultati.get(rezultati.size() - 1));
            rezultati.add(xk1);
            t += T;
            if (iter % iterStep == 0) {
                System.out.println("t=" + t + " X" + iter + ":");
                System.out.println(xk1);
            }

            iter++;
        }

        try {
            Path path = Paths.get("preview.ipynb");
            Files.write(path, Files.readAllLines(path).stream().map(s -> {
                if (s.trim().startsWith("\"X1=")) {
                    return "\"X1=" + Arrays.toString(rezultati.stream().mapToDouble(x -> x.get(0, 0)).toArray())+"\n\",";
                } else if (s.trim().startsWith("\"X2=")) {
                    return "\"X2=" + Arrays.toString(rezultati.stream().mapToDouble(x -> x.get(1, 0)).toArray())+"\n\",";
                } else {
                    return s;
                }
            }).collect(Collectors.toList()), Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected abstract Matrix calculateXk1(Matrix xk);
}
