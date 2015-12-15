package model;

import ann.BackpropAlg;
import ann.FFANN;
import ann.SigmoidTransferFunction;

import java.io.File;
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
 * Created by ivan on 12/12/15.
 */
public class Model {

    public static final String SUFFIX = ".char";
    public static final String ROOT_PATH = "res";
    private static final String WEIGHTS_FILE = "weights.dat";
    private List<CharacterObject> mCharacters;
    private double[] mWeights;
    private FFANN mFfann;
    private int mFeatureSize;

    public Model() {
        mCharacters = new ArrayList<>();
        mFeatureSize = 10;
        mFfann = FFANN.create(new int[]{mFeatureSize, mFeatureSize/2, Clazz.values().length}, new SigmoidTransferFunction());
        Path path = Paths.get(ROOT_PATH, WEIGHTS_FILE);
        try {
            if (Files.exists(path)) {
                List<String> strings = Files.readAllLines(path);
                mWeights = Arrays.stream(strings.get(0).trim().split(",")).mapToDouble(Double::valueOf).toArray();
            }
        } catch (IOException e) {

        }

    }


    public void onPointsCollected(Clazz canvasClazz, int[] xes, int[] yes, boolean isTest) {
        CharacterObject character = new CharacterObject(canvasClazz, xes, yes);
        if(!isTest) {
            mCharacters.add(character);
            try {
                character.toFile(Paths.get(ROOT_PATH, canvasClazz.name(), Long.toHexString(System.currentTimeMillis()) + SUFFIX).toFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            if (mWeights != null) {
                double[] outputs = new double[Clazz.values().length];
                mFfann.calcOutputs(character.getFeatures(mFeatureSize), mWeights, outputs);

                int argmax = 0;
                double max = 0;
                for (int i = 0; i < outputs.length; i++) {
                    if (max < outputs[i]) {
                        max = outputs[i];
                        argmax = i;
                    }
                }
                System.out.println(Clazz.values()[argmax].name() + " | " + max);

                System.out.println(Arrays.toString(outputs));
            }
        }
    }

    public void onUndo() {
        if (mCharacters.size() > 0) {
            mCharacters.remove(mCharacters.size() - 1).deleteFromDisk();
        }
    }

    public void onTrainSelected(double eta, double epsilon, int maxIterCount) {
        if(mCharacters.size()>0) {

            List<double[][]> rawDataset = mCharacters.stream().map(x -> {
                double[] output = new double[Clazz.values().length];
                output[x.getClazzId()] = 1;
                return new double[][]{x.getFeatures(mFeatureSize), output};
            }).collect(Collectors.toList());

            BatchReadOnlyDataset batchReadOnlyDataset = new BatchReadOnlyDataset(rawDataset, 1);

            BackpropAlg alg = new BackpropAlg(mFfann, batchReadOnlyDataset,eta);
            mWeights = alg.run(epsilon, maxIterCount);
            System.out.println(Arrays.toString(mWeights));

            double error = alg.calculateError(batchReadOnlyDataset.getWhole(), mWeights);
            List<String> strings = new ArrayList<>();
            strings.add(String.join(",",String.join(",", Arrays.stream(mWeights).mapToObj(Double::toString).collect(Collectors.toList()))));
            strings.add(Double.toString(error));
            try {
                Files.write(Paths.get(ROOT_PATH,WEIGHTS_FILE),strings, Charset.defaultCharset());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    public void onTestSelected() {
//        if(mWeights!=null){
//            double[] outputs = new double[Clazz.values().length];
//            mFfann.calcOutputs(mCharacters.get(mCharacters.size()-1).getFeatures(mFeatureSize),mWeights,outputs);
//            System.out.println(Arrays.toString(outputs));
//        }
//    }

    public String getDatasetInfo() {
        int[] count = new int[Clazz.values().length];
        for (CharacterObject c : mCharacters) {
            count[c.getClazzId()]++;
        }
        StringBuilder sb = new StringBuilder();
        for (Clazz c : Clazz.values()) {
            sb.append(c.name()).append("[").append(count[c.ordinal()]).append("]\n");
        }
        return sb.toString();
    }

    public void preloadFromDisk() throws IOException {
        String root = ROOT_PATH;
        for (Clazz c:Clazz.values()) {
            Path dir = Paths.get(root,c.name());
            if(!Files.exists(dir)){
                Files.createDirectory(dir);
            } else{
                File[] files = dir.toFile().listFiles((dir1, name) -> {
                    return name.endsWith(SUFFIX);
                });

                for(File file : files){
                    mCharacters.add(CharacterObject.fromFile(file));
                }
            }
        }
    }

    public double getSize() {
        return mCharacters.size();
    }
}
