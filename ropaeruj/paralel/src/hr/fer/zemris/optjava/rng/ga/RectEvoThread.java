package hr.fer.zemris.optjava.rng.ga;

import hr.fer.zemris.optjava.rng.image.GrayScaleImage;

import java.io.File;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by ivan on 1/16/16.
 */
public class RectEvoThread extends EvoThread {
    private final File imageFile;
    private BlockingQueue<RectSolution> requestQueue;
    private BlockingQueue<RectSolution> responseQueue;
    private ThreadLocal<Evaluator> evaluator = new ThreadLocal<>();

    public RectEvoThread(BlockingQueue<RectSolution> requestQueue,BlockingQueue<RectSolution> responseQueue,File imageFile) {
        this.requestQueue = requestQueue;
        this.responseQueue = responseQueue;
        this.imageFile = imageFile;
    }

    @Override
    public void run() {
        try {
            while(true) {
                RectSolution solution = requestQueue.take();
                getEvaluator().evaluate(solution);
                responseQueue.put(solution);
            }
        } catch (InterruptedException e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }

    public Evaluator getEvaluator(){
        if(evaluator.get()==null){
            synchronized (imageFile){
                try {
                    evaluator.set(new Evaluator(GrayScaleImage.load(imageFile)));
                } catch (IOException e) {
                    System.out.println(e);
                    throw new RuntimeException(e);
                }
            }
        }
        return evaluator.get();
    }
}
